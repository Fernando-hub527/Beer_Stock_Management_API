package com.cervejaria.gerenciamento.cervejaria.controller;

import com.cervejaria.gerenciamento.cervejaria.builder.BeerDTOBuilder;
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
//    @Test
//    void whenPOSTBeerIsCalledThenBeerIsCreat() throws Exception {
//        //given
//        GroupBeerDTO groupBeerDTOForCreate = BeerDTOBuilder.builder().build().toBeerDTO();
//
//
//        //when
//        when(groupBeerService.createBeer(groupBeerDTOForCreate)).thenReturn(groupBeerDTOForCreate);
//
//        //then
//        mockMvc.perform(post(BEER_API_URL_PATH + "/beer/group")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(groupBeerDTOForCreate)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name", is(groupBeerDTOForCreate.getName())))
//                .andExpect(jsonPath("$.brand", is(groupBeerDTOForCreate.getBrand())))
//                .andExpect(jsonPath("$.type", is(groupBeerDTOForCreate.getType().toString())));
//
//    }

    @Test
    void whenPOSTGroupIsCalledWithoutRequiredFieldAnErrorIsReturned() throws Exception {
        GroupBeerDTO groupBeerDTOForCreate = BeerDTOBuilder.builder().build().toBeerDTO();
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
        mockMvc.perform(post(BEER_API_URL_PATH + "/provider/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(provideDTOForCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.telephone", is(provideDTOForCreate.getTelephone())))
                .andExpect(jsonPath("$.name", is(provideDTOForCreate.getName())))
                .andExpect(jsonPath("$.address", is(provideDTOForCreate.getAddress())));

    }

    @Test
    void whenPOSTProviderIsCalledWithoutRequiredFieldAnErrorIsReturned() throws Exception {
        GroupBeerDTO groupBeerDTOForCreate = BeerDTOBuilder.builder().build().toBeerDTO();
        groupBeerDTOForCreate.setBrand(null);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH + "/beer/provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupBeerDTOForCreate)))
                .andExpect(status().isBadRequest());
    }

    //findByName

    @Test
    void whenGetIsCalledWithNameValidThenOKIsReturned() throws Exception {
        //given
        GroupBeerDTO groupBeerDTOForSearch = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.findByName(groupBeerDTOForSearch.getName())).thenReturn(groupBeerDTOForSearch);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + groupBeerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(groupBeerDTOForSearch.getName())))
                .andExpect(jsonPath("$.brand", is(groupBeerDTOForSearch.getBrand())))
                .andExpect(jsonPath("$.type", is(groupBeerDTOForSearch.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        //given
        GroupBeerDTO groupBeerDTOForSearch = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.findByName(groupBeerDTOForSearch.getName())).thenThrow(BeerNotFoundException.class);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + groupBeerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //listBeers

    @Test
    void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        GroupBeerDTO groupBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.listAll()).thenReturn(Collections.singletonList(groupBeerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(groupBeerDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(groupBeerDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(groupBeerDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        GroupBeerDTO groupBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(groupBeerService.listAll()).thenReturn(Collections.singletonList(groupBeerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test void whenDeletedIsCalledWithIdValidThenNoContentIsReturned() throws Exception {
        GroupBeerDTO groupBeerDTOForDeleted = BeerDTOBuilder.builder().build().toBeerDTO();

        doNothing().when(groupBeerService).deleteById(groupBeerDTOForDeleted.getId());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + groupBeerDTOForDeleted.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

//    @Test void whenDeletedIsCalledWithIdValidThenNotFoundIsReturned() throws Exception {
//        GroupBeerDTO beerDTOForDeleted = BeerDTOBuilder.builder().build().toBeerDTO();
//
//        doThrow(BeerNotFoundException.class).when(groupBeerService).deleteById(INVALid_BEER_ID);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + beerDTOForDeleted.getId())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }


    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        GroupBeerDTO groupBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
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

        GroupBeerDTO groupBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
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

        GroupBeerDTO groupBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
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

        GroupBeerDTO groupBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
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










