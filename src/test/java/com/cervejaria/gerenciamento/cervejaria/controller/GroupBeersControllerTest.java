package com.cervejaria.gerenciamento.cervejaria.controller;

import com.cervejaria.gerenciamento.cervejaria.builder.GroupBeerDTOBuilder;
import com.cervejaria.gerenciamento.cervejaria.builder.ProviderDTOBuider;
import com.cervejaria.gerenciamento.cervejaria.dto.GroupBeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.QuantityDTO;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerStockExceededException;
import com.cervejaria.gerenciamento.cervejaria.service.GroupBeerService;
import com.cervejaria.gerenciamento.cervejaria.service.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static com.cervejaria.gerenciamento.cervejaria.utils.JsonConvertionUtils.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@ExtendWith(MockitoExtension.class)
class GroupBeersControllerTest {

    private static final String BEER_API_URL_PATH = "/api/v1/beers";
    private static final Long VALID_BEER_ID = 1l;
    private static final Long INVALid_BEER_ID = 2l;
    private static final String BEER_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String BEER_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private GroupBeerService groupBeerService;


    @Mock
    private ProviderService providerService;

    @InjectMocks
    private com.cervejaria.gerenciamento.cervejaria.controller.APIBeerController APIBeerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(APIBeerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    //createBeer
    @Test
    void whenPOSTBeerIsCalledThenBeerIsCreat() throws Exception {
        //given
        GroupBeerDTO groupBeerDTOForCreate = GroupBeerDTOBuilder.builder().build().toBeerDTO();


        //when
        when(groupBeerService.createGroupBeer(groupBeerDTOForCreate)).thenReturn(groupBeerDTOForCreate);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH + "/beer/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupBeerDTOForCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(groupBeerDTOForCreate.getName())))
                .andExpect(jsonPath("$.brand", is(groupBeerDTOForCreate.getBrand())))
                .andExpect(jsonPath("$.type", is(groupBeerDTOForCreate.getType().toString())));

    }

    @Test
    void whenPOSTGroupIsCalledWithoutRequiredFieldAnErrorIsReturned() throws Exception {
        GroupBeerDTO groupBeerDTOForCreate = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        groupBeerDTOForCreate.setBrand(null);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH + "/beer/group")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupBeerDTOForCreate)))
                .andExpect(status().isBadRequest());
    }

    //createProvider
    @Test
    void whenPOSTProviderIsCalledThenBeerIsCreat() throws Exception {
        //given
        ProvideDTO provideDTOForCreate = ProviderDTOBuider.builder().build().toProviderDTO();

        //when
        when(providerService.createProvider(provideDTOForCreate)).thenReturn(provideDTOForCreate);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH + "/people/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(provideDTOForCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.telephone", is(provideDTOForCreate.getTelephone())))
                .andExpect(jsonPath("$.name", is(provideDTOForCreate.getName())))
                .andExpect(jsonPath("$.address", is(provideDTOForCreate.getAddress())));

    }

    @Test
    void whenPOSTProviderIsCalledWithoutRequiredFieldAnErrorIsReturned() throws Exception {
        ProvideDTO providerDTOForCreate = ProviderDTOBuider.builder().build().toProviderDTO();
        providerDTOForCreate.setName(null);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH + "/people/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(providerDTOForCreate)))
                .andExpect(status().isBadRequest());
    }

    //findByName

    @Test
    void whenGetIsCalledWithNameValidThenOKIsReturned() throws Exception {
        //given
        GroupBeerDTO groupBeerDTOForSearch = GroupBeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.findByName(groupBeerDTOForSearch.getName())).thenReturn(groupBeerDTOForSearch);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/groupBeer/" + groupBeerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(groupBeerDTOForSearch.getName())))
                .andExpect(jsonPath("$.brand", is(groupBeerDTOForSearch.getBrand())))
                .andExpect(jsonPath("$.type", is(groupBeerDTOForSearch.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        //given
        GroupBeerDTO groupBeerDTOForSearch = GroupBeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.findByName(groupBeerDTOForSearch.getName())).thenThrow(BeerNotFoundException.class);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/groupBeer/" + groupBeerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    //findByName

    @Test
    void whenGetProviderIsCalledWithNameValidThenOKIsReturned() throws Exception {
        //given
        ProvideDTO providerDTOForSearch = ProviderDTOBuider.builder().build().toProviderDTO();

        //when
        when(providerService.findByName(providerDTOForSearch.getName())).thenReturn(providerDTOForSearch);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/provider/" + providerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(providerDTOForSearch.getName())))
                .andExpect(jsonPath("$.cnpj", is(providerDTOForSearch.getCnpj())))
                .andExpect(jsonPath("$.telephone", is(providerDTOForSearch.getTelephone())));
    }

    @Test
    void whenGETProviderIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        //given
        ProvideDTO providerDTOForSearch = ProviderDTOBuider.builder().build().toProviderDTO();

        //when
        when(providerService.findByName(providerDTOForSearch.getName())).thenThrow(BeerNotFoundException.class);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/provider/" + providerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //listBeers

    @Test
    void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        GroupBeerDTO groupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.listAll()).thenReturn(Collections.singletonList(groupBeerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/beer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(groupBeerDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(groupBeerDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(groupBeerDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        GroupBeerDTO groupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.listAll()).thenReturn(Collections.singletonList(groupBeerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/beer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //listProvider

    @Test
    void whenGETListWithProviderIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        ProvideDTO providerDTO = ProviderDTOBuider.builder().build().toProviderDTO();

        //when
        when(providerService.listAll()).thenReturn(Collections.singletonList(providerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/provider")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(providerDTO.getName())))
                .andExpect(jsonPath("$[0].cnpj", is(providerDTO.getCnpj())))
                .andExpect(jsonPath("$[0].telephone", is(providerDTO.getTelephone())));
    }

    @Test
    void whenGETListWithoutProvidersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        ProvideDTO provideDTO = ProviderDTOBuider.builder().build().toProviderDTO();

        //when
        when(providerService.listAll()).thenReturn(Collections.singletonList(provideDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/provider")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //deleteBeer

    @Test void whenDeletedIsCalledWithIdValidThenNoContentIsReturned() throws Exception {
        GroupBeerDTO groupBeerDTOForDeleted = GroupBeerDTOBuilder.builder().build().toBeerDTO();

        doNothing().when(groupBeerService).deleteById(groupBeerDTOForDeleted.getId());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + groupBeerDTOForDeleted.getId() + "/beer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test void whenDeletedIsCalledWithIdValidThenNotFoundIsReturned() throws Exception {
        GroupBeerDTO beerDTOForDeleted = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        beerDTOForDeleted.setId(2L);

        doThrow(BeerNotFoundException.class).when(groupBeerService).deleteById(INVALid_BEER_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + beerDTOForDeleted.getId() + "/beer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //deleteProvider
    @Test void whenDeletedProviderIsCalledWithIdValidThenNoContentIsReturned() throws Exception {
        ProvideDTO provideDTOForDeleted = ProviderDTOBuider.builder().build().toProviderDTO();

        doNothing().when(providerService).deleteById(provideDTOForDeleted.getCnpj());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + provideDTOForDeleted.getCnpj() + "/provider")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test void whenDeletedProviderIsCalledWithIdValidThenNotFoundIsReturned() throws Exception {
        ProvideDTO provideDTOForDeleted = ProviderDTOBuider.builder().build().toProviderDTO();
        provideDTOForDeleted.setCnpj("2");

        doThrow(BeerNotFoundException.class).when(providerService).deleteById("2");

        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + provideDTOForDeleted.getCnpj() + "/provider")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        GroupBeerDTO groupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        groupBeerDTO.setQuantity(groupBeerDTO.getQuantity() + quantityDTO.getQuantity());

        when(groupBeerService.increment(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(groupBeerDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(groupBeerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(groupBeerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(groupBeerDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(groupBeerDTO.getQuantity())));
    }


    @Test
    void whenPATCHIsCalledWithInvalidBeerIdToIncrementThenNotFoundStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(30)
                .build();

        when(groupBeerService.increment(INVALid_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALid_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToDecrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(5)
                .build();

        GroupBeerDTO groupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        groupBeerDTO.setQuantity(groupBeerDTO.getQuantity() + quantityDTO.getQuantity());

        when(groupBeerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(groupBeerDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(groupBeerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(groupBeerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(groupBeerDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(groupBeerDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(30)
                .build();

        GroupBeerDTO groupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        groupBeerDTO.setQuantity(groupBeerDTO.getQuantity() + quantityDTO.getQuantity());

        when(groupBeerService.increment(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(60)
                .build();

        GroupBeerDTO groupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        groupBeerDTO.setQuantity(groupBeerDTO.getQuantity() + quantityDTO.getQuantity());

        when(groupBeerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledWithInvalidBeerIdToDecrementThenNotFoundStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(5)
                .build();

        when(groupBeerService.decrement(INVALid_BEER_ID, quantityDTO.getQuantity()))
                .thenThrow(BeerNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + INVALid_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isNotFound());
    }
}










