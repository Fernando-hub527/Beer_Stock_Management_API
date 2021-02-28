package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.builder.BeerDTOBuilder;
import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.Beer;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.mapper.BeerMapper;
import com.cervejaria.gerenciamento.cervejaria.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class BeerServiceTest {

    @Mock
    private BeerRepository beerRepository;
    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;


    @Test
    void whenCreateBeerIsCalledThenBeerIsCreate() throws BeerAlreadyRegisteredException {
        BeerDTO beerDTOExpected = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer beerSaved = beerMapper.toModel(beerDTOExpected);

        //força os retornos do objeto simulado
        when(beerRepository.findByName(beerDTOExpected.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(beerSaved)).thenReturn(beerSaved);

        //cria o serviço com a cerveja modelo(BeerDTOBuilder)
        BeerDTO beerCreate = beerService.createBeer(beerDTOExpected);

        //confere os resultados
        assertThat(beerCreate.getName(), is(equalTo(beerDTOExpected.getName())));
        assertThat(beerCreate.getId(), is(equalTo(beerDTOExpected.getId())));
        assertThat(beerCreate, is(equalTo(beerDTOExpected)));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() throws BeerAlreadyRegisteredException {
        BeerDTO beerDTOExpected = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer duplicateBeer = beerMapper.toModel(beerDTOExpected);

        //simula a existencia do registro inserido
        when(beerRepository.findByName(beerDTOExpected.getName())).thenReturn(Optional.of(duplicateBeer));

        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(beerDTOExpected));
    }

    //findByName
    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        BeerDTO beerDTOExpected = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer beerExpected = beerMapper.toModel(beerDTOExpected);

        //força o retorno de um nome registrado
        when(beerRepository.findByName(beerExpected.getName())).thenReturn(Optional.of(beerExpected));

        BeerDTO beerReturned = beerService.findByName(beerDTOExpected.getName());

        assertThat(beerReturned.getName(), is(equalTo(beerExpected.getName())));

    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerRepository.findByName(expectedFoundBeerDTO.getName())).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(expectedFoundBeerDTO.getName()));

    }

    //ListAll
    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        BeerDTO beerDTOListExpected = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer beerListExpected = beerMapper.toModel(beerDTOListExpected);

        when(beerRepository.findAll()).thenReturn(Collections.singletonList(beerListExpected));

        List<BeerDTO> listBeers = beerService.listAll();

        assertThat(listBeers, is(not(empty())));
        assertThat(listBeers.get(0), is(equalTo(beerDTOListExpected)));
    }

    //delete

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
        BeerDTO beerDTOForBerDeleted = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer beerforBeDeleted = beerMapper.toModel(beerDTOForBerDeleted);

        when(beerRepository.findById(beerDTOForBerDeleted.getId())).thenReturn(Optional.of(beerforBeDeleted));
        doNothing().when(beerRepository).deleteById(beerDTOForBerDeleted.getId());

        beerService.deleteById(beerDTOForBerDeleted.getId());

        verify(beerRepository, times(1)).findById(beerDTOForBerDeleted.getId());
        verify(beerRepository, times(1)).deleteById(beerDTOForBerDeleted.getId());
    }


}

















