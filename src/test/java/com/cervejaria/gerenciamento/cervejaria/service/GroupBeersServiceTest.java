package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.builder.GroupBeerDTOBuilder;
import com.cervejaria.gerenciamento.cervejaria.dto.GroupBeerDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerStockExceededException;
import com.cervejaria.gerenciamento.cervejaria.mapper.GroupBeerMapper;
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
    private GroupBeerMapper groupBeerMapper = GroupBeerMapper.INSTANCE;

    @InjectMocks
    private GroupBeerService groupBeerService;


    @Test
    void whenCreateBeerIsCalledThenBeerIsCreate() throws BeerAlreadyRegisteredException {
        GroupBeerDTO groupBeerDTOExpected = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers groupBeersSaved = groupBeerMapper.toBeerModel(groupBeerDTOExpected);

        //força os retornos do objeto simulado
        when(groupBeerRepository.findByName(groupBeerDTOExpected.getName())).thenReturn(Optional.empty());
        when(groupBeerRepository.save(groupBeersSaved)).thenReturn(groupBeersSaved);

        //cria o serviço com a cerveja modelo(GroupBeerDTOBuilder)
        GroupBeerDTO beerCreate = groupBeerService.createGroupBeer(groupBeerDTOExpected);

        //confere os resultados
        assertThat(beerCreate.getName(), is(equalTo(groupBeerDTOExpected.getName())));
        assertThat(beerCreate.getId(), is(equalTo(groupBeerDTOExpected.getId())));
        assertThat(beerCreate, is(equalTo(groupBeerDTOExpected)));
    }

//    @Test
//    void whenAlreadyRegisteredGroupBeerInformedThenAnExceptionShouldBeThrown() throws BeerAlreadyRegisteredException {
//        GroupBeerDTO groupBeerDTOExpected = GroupBeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers duplicateGroupBeers = groupBeerMapper.toBeerModel(groupBeerDTOExpected);
//
//        //simula a existencia do registro inserido
//        when(groupBeerRepository.findByName(groupBeerDTOExpected.getName())).thenReturn(Optional.of(duplicateGroupBeers));
//
//        assertThrows(BeerAlreadyRegisteredException.class, () -> groupBeerService.createBeer(groupBeerDTOExpected));
//    }

    //findByName
    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
        GroupBeerDTO groupBeerDTOExpected = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers groupBeersExpected = groupBeerMapper.toBeerModel(groupBeerDTOExpected);

        //força o retorno de um nome registrado
        when(groupBeerRepository.findByName(groupBeersExpected.getName())).thenReturn(Optional.of(groupBeersExpected));

        GroupBeerDTO beerReturned = groupBeerService.findByName(groupBeerDTOExpected.getName());

        assertThat(beerReturned.getName(), is(equalTo(groupBeersExpected.getName())));

    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        GroupBeerDTO expectedFoundGroupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();

        when(groupBeerRepository.findByName(expectedFoundGroupBeerDTO.getName())).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> groupBeerService.findByName(expectedFoundGroupBeerDTO.getName()));

    }

    //ListAll
    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        GroupBeerDTO groupBeerDTOListExpected = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers groupBeersListExpected = groupBeerMapper.toBeerModel(groupBeerDTOListExpected);

        when(groupBeerRepository.findAll()).thenReturn(Collections.singletonList(groupBeersListExpected));

        List<GroupBeerDTO> listBeers = groupBeerService.listAll();

        assertThat(listBeers, is(not(empty())));
        assertThat(listBeers.get(0), is(equalTo(groupBeerDTOListExpected)));
    }

    //delete

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
        GroupBeerDTO groupBeerDTOForBerDeleted = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers beerforBeDeleted = groupBeerMapper.toBeerModel(groupBeerDTOForBerDeleted);

        when(groupBeerRepository.findById(groupBeerDTOForBerDeleted.getId())).thenReturn(Optional.of(beerforBeDeleted));
        doNothing().when(groupBeerRepository).deleteById(groupBeerDTOForBerDeleted.getId());

        groupBeerService.deleteById(groupBeerDTOForBerDeleted.getId());

        verify(groupBeerRepository, times(1)).findById(groupBeerDTOForBerDeleted.getId());
        verify(groupBeerRepository, times(1)).deleteById(groupBeerDTOForBerDeleted.getId());
    }

    //increment

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        //given
        GroupBeerDTO expectedGroupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = groupBeerMapper.toBeerModel(expectedGroupBeerDTO);

        //when
        when(groupBeerRepository.findById(expectedGroupBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
        when(groupBeerRepository.save(expectedGroupBeers)).thenReturn(expectedGroupBeers);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedGroupBeerDTO.getQuantity() + quantityToIncrement;

        // then
        GroupBeerDTO incrementedGroupBeerDTO = groupBeerService.increment(expectedGroupBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedGroupBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedGroupBeerDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        GroupBeerDTO expectedGroupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = groupBeerMapper.toBeerModel(expectedGroupBeerDTO);

        when(groupBeerRepository.findById(expectedGroupBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));

        int quantityToIncrement = 80;
        assertThrows(BeerStockExceededException.class, () -> groupBeerService.increment(expectedGroupBeerDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        GroupBeerDTO expectedGroupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = groupBeerMapper.toBeerModel(expectedGroupBeerDTO);

        when(groupBeerRepository.findById(expectedGroupBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));

        int quantityToIncrement = 45;
        assertThrows(BeerStockExceededException.class, () -> groupBeerService.increment(expectedGroupBeerDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(groupBeerRepository.findById(INVALID_BEER_ID))
                .thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> groupBeerService.increment(INVALID_BEER_ID, quantityToIncrement));
    }




    @Test
    void whenDecrementIsCalledThenDecrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        GroupBeerDTO expectedGroupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = groupBeerMapper.toBeerModel(expectedGroupBeerDTO);

        when(groupBeerRepository.findById(expectedGroupBeerDTO.getId()))
                .thenReturn(Optional.of(expectedGroupBeers));

        when(groupBeerRepository.save(expectedGroupBeers))
                .thenReturn(expectedGroupBeers);

        int quantityToDecrement = 5;
        int expectedQuantityAfterDecrement = expectedGroupBeerDTO.getQuantity() - quantityToDecrement;
        GroupBeerDTO decrementedGroupBeerDTO = groupBeerService.decrement(expectedGroupBeerDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(decrementedGroupBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
    }

    //decrement

        @Test
    void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws BeerNotFoundException, BeerStockExceededException {
        GroupBeerDTO expectedGroupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = groupBeerMapper.toBeerModel(expectedGroupBeerDTO);

        when(groupBeerRepository.findById(expectedGroupBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
        when(groupBeerRepository.save(expectedGroupBeers)).thenReturn(expectedGroupBeers);

        int quantityToDecrement = 10;
        int expectedQuantityAfterDecrement = expectedGroupBeerDTO.getQuantity() - quantityToDecrement;
        GroupBeerDTO incrementedGroupBeerDTO = groupBeerService.decrement(expectedGroupBeerDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterDecrement, equalTo(0));
        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedGroupBeerDTO.getQuantity()));
    }

    @Test
    void whenDecrementIsLowerThanZeroThenThrowException() {
        GroupBeerDTO expectedGroupBeerDTO = GroupBeerDTOBuilder.builder().build().toBeerDTO();
        GroupBeers expectedGroupBeers = groupBeerMapper.toBeerModel(expectedGroupBeerDTO);

        when(groupBeerRepository.findById(expectedGroupBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));

        int quantityToDecrement = 80;
        assertThrows(BeerStockExceededException.class, () -> groupBeerService.decrement(expectedGroupBeerDTO.getId(), quantityToDecrement));
    }

    @Test
    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToDecrement = 10;

        when(groupBeerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> groupBeerService.decrement(INVALID_BEER_ID, quantityToDecrement));
    }
}

















