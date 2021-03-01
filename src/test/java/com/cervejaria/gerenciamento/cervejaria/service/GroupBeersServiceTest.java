package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.builder.BeerDTOBuilder;
import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerStockExceededException;
import com.cervejaria.gerenciamento.cervejaria.mapper.BeerMapper;
import com.cervejaria.gerenciamento.cervejaria.repository.GroupBeerRepository;
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
class GroupBeersServiceTest {

    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private GroupBeerRepository groupBeerRepository;
    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;


    @Test
    void whenCreateBeerIsCalledThenBeerIsCreate() throws BeerAlreadyRegisteredException {
        BeerDTO beerDTOExpected = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers groupBeersSaved = beerMapper.toModel(beerDTOExpected);

        //força os retornos do objeto simulado
        when(groupBeerRepository.findByName(beerDTOExpected.getName())).thenReturn(Optional.empty());
        when(groupBeerRepository.save(groupBeersSaved)).thenReturn(groupBeersSaved);

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
        GroupBeers duplicateGroupBeers = beerMapper.toModel(beerDTOExpected);

        //simula a existencia do registro inserido
        when(groupBeerRepository.findByName(beerDTOExpected.getName())).thenReturn(Optional.of(duplicateGroupBeers));

        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(beerDTOExpected));
    }

    //findByName
    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        BeerDTO beerDTOExpected = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers groupBeersExpected = beerMapper.toModel(beerDTOExpected);

        //força o retorno de um nome registrado
        when(groupBeerRepository.findByName(groupBeersExpected.getName())).thenReturn(Optional.of(groupBeersExpected));

        BeerDTO beerReturned = beerService.findByName(beerDTOExpected.getName());

        assertThat(beerReturned.getName(), is(equalTo(groupBeersExpected.getName())));

    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(groupBeerRepository.findByName(expectedFoundBeerDTO.getName())).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(expectedFoundBeerDTO.getName()));

    }

    //ListAll
    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        BeerDTO beerDTOListExpected = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers groupBeersListExpected = beerMapper.toModel(beerDTOListExpected);

        when(groupBeerRepository.findAll()).thenReturn(Collections.singletonList(groupBeersListExpected));

        List<BeerDTO> listBeers = beerService.listAll();

        assertThat(listBeers, is(not(empty())));
        assertThat(listBeers.get(0), is(equalTo(beerDTOListExpected)));
    }

    //delete

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
        BeerDTO beerDTOForBerDeleted = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers beerforBeDeleted = beerMapper.toModel(beerDTOForBerDeleted);

        when(groupBeerRepository.findById(beerDTOForBerDeleted.getId())).thenReturn(Optional.of(beerforBeDeleted));
        doNothing().when(groupBeerRepository).deleteById(beerDTOForBerDeleted.getId());

        beerService.deleteById(beerDTOForBerDeleted.getId());

        verify(groupBeerRepository, times(1)).findById(beerDTOForBerDeleted.getId());
        verify(groupBeerRepository, times(1)).deleteById(beerDTOForBerDeleted.getId());
    }

    //increment

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);

        //when
        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
        when(groupBeerRepository.save(expectedGroupBeers)).thenReturn(expectedGroupBeers);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;

        // then
        BeerDTO incrementedBeerDTO = beerService.increment(expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);

        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));

        int quantityToIncrement = 80;
        assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);

        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));

        int quantityToIncrement = 45;
        assertThrows(BeerStockExceededException.class, () -> beerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(groupBeerRepository.findById(INVALID_BEER_ID))
                .thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerService.increment(INVALID_BEER_ID, quantityToIncrement));
    }




    @Test
    void whenDecrementIsCalledThenDecrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);

        when(groupBeerRepository.findById(expectedBeerDTO.getId()))
                .thenReturn(Optional.of(expectedGroupBeers));

        when(groupBeerRepository.save(expectedGroupBeers))
                .thenReturn(expectedGroupBeers);

        int quantityToDecrement = 5;
        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
        BeerDTO decrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(decrementedBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
    }

    //decrement

        @Test
    void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);

        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
        when(groupBeerRepository.save(expectedGroupBeers)).thenReturn(expectedGroupBeers);

        int quantityToDecrement = 10;
        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(0));
        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
    }

    @Test
    void whenDecrementIsLowerThanZeroThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);

        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));

        int quantityToDecrement = 80;
        assertThrows(BeerStockExceededException.class, () -> beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement));
    }

    @Test
    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToDecrement = 10;

        when(groupBeerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> beerService.decrement(INVALID_BEER_ID, quantityToDecrement));
    }
}

















