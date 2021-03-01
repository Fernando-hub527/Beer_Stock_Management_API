package com.cervejaria.gerenciamento.cervejaria.controller;

import com.cervejaria.gerenciamento.cervejaria.builder.BeerDTOBuilder;
import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.QuantityDTO;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerStockExceededException;
import com.cervejaria.gerenciamento.cervejaria.service.BeerService;
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
    private BeerService beerService;

    @InjectMocks
    private APIAPIBeerController APIBeerController;

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
        BeerDTO beerDTOForCreate = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerService.createBeer(beerDTOForCreate)).thenReturn(beerDTOForCreate);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDTOForCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(beerDTOForCreate.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTOForCreate.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDTOForCreate.getType().toString())));

    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldAnErrorIsReturned() throws Exception {
        BeerDTO beerDTOForCreate = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTOForCreate.setBrand(null);

        //then
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(beerDTOForCreate)))
                .andExpect(status().isBadRequest());
    }

    //findByName

    @Test
    void whenGetIsCalledWithNameValidThenOKIsReturned() throws Exception {
        //given
        BeerDTO beerDTOForSearch = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerService.findByName(beerDTOForSearch.getName())).thenReturn(beerDTOForSearch);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + beerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTOForSearch.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTOForSearch.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDTOForSearch.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        //given
        BeerDTO beerDTOForSearch = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerService.findByName(beerDTOForSearch.getName())).thenThrow(BeerNotFoundException.class);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH + "/" + beerDTOForSearch.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //listBeers

    @Test
    void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerService.listAll()).thenReturn(Collections.singletonList(beerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(beerDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(beerDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerService.listAll()).thenReturn(Collections.singletonList(beerDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test void whenDeletedIsCalledWithIdValidThenNoContentIsReturned() throws Exception {
        BeerDTO beerDTOForDeleted = BeerDTOBuilder.builder().build().toBeerDTO();

        doNothing().when(beerService).deleteById(beerDTOForDeleted.getId());

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL_PATH + "/" + beerDTOForDeleted.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

//    @Test void whenDeletedIsCalledWithIdValidThenNotFoundIsReturned() throws Exception {
//        BeerDTO beerDTOForDeleted = BeerDTOBuilder.builder().build().toBeerDTO();
//
//        doThrow(BeerNotFoundException.class).when(beerService).deleteById(INVALid_BEER_ID);
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

        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        when(beerService.increment(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())));
    }


    @Test
    void whenPATCHIsCalledWithInvalidBeerIdToIncrementThenNotFoundStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(30)
                .build();

        when(beerService.increment(INVALid_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);
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

        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        when(beerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToIncrementGreatherThanMaxThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(30)
                .build();

        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        when(beerService.increment(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledToDEcrementLowerThanZeroThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(60)
                .build();

        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        when(beerService.decrement(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledWithInvalidBeerIdToDecrementThenNotFoundStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(5)
                .build();

        when(beerService.decrement(INVALid_BEER_ID, quantityDTO.getQuantity()))
                .thenThrow(BeerNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.patch(BEER_API_URL_PATH + "/" + INVALid_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO)))
                .andExpect(status().isNotFound());
    }
}










