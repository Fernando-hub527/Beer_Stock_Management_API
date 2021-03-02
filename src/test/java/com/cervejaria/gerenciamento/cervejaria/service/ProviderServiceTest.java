package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.builder.BeerDTOBuilder;
import com.cervejaria.gerenciamento.cervejaria.builder.ProviderDTOBuider;
import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.entity.Provide;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerStockExceededException;
import com.cervejaria.gerenciamento.cervejaria.mapper.BeerMapper;
import com.cervejaria.gerenciamento.cervejaria.mapper.ProvideMapper;
import com.cervejaria.gerenciamento.cervejaria.repository.GroupBeerRepository;
import com.cervejaria.gerenciamento.cervejaria.repository.ProvideRepository;
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

   // private static final long INVALID_BEER_ID = 1L;

    @Mock
    private ProvideRepository provideRepository;

    @Mock
    private ProvideMapper provideMapper = ProvideMapper.INSTANCE;

    @InjectMocks
    private ProviderService providerService;


    @Test
    void whenCreateProviderIsCalledThenProviderIsCreate() throws BeerAlreadyRegisteredException {
        //given
        ProvideDTO provideDTOExpected = ProviderDTOBuider.builder().build().toProviderDTO();
        Provide provideForSave = provideMapper.toModel(provideDTOExpected);

        //when
        when(provideRepository.findByName(provideDTOExpected.getName())).thenReturn(Optional.empty());
        when(provideRepository.save(provideForSave)).thenReturn(provideForSave);

        //then
        ProvideDTO provideDTOsaved= providerService.createProvider(provideDTOExpected);

        assertThat(provideDTOsaved.getName(), is(equalTo(provideDTOExpected.getName())));

    }
//
//    @Test
//    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() throws BeerAlreadyRegisteredException {
//        ProvideDTO providerDTOExpected = ProviderDTOBuider.builder().build().toProviderDTO();
//        Provide duplicateGroupBeers = provideMapper.toModel(providerDTOExpected);
//
//        //simula a existencia do registro inserido
//        when(provideRepository.findByName(providerDTOExpected.getName())).thenReturn(Optional.of(duplicateGroupBeers));
//
//        assertThrows(BeerAlreadyRegisteredException.class, () -> providerService.createProvider(providerDTOExpected));
//    }
//
//    //findByName
//    @Test
//    void whenValidBeerNameIsGivenThenReturnABeer() throws BeerNotFoundException {
//        BeerDTO beerDTOExpected = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers groupBeersExpected = beerMapper.toModel(beerDTOExpected);
//
//        //força o retorno de um nome registrado
//        when(groupBeerRepository.findByName(groupBeersExpected.getName())).thenReturn(Optional.of(groupBeersExpected));
//
//        BeerDTO beerReturned = groupBeerService.findByName(beerDTOExpected.getName());
//
//        assertThat(beerReturned.getName(), is(equalTo(groupBeersExpected.getName())));
//
//    }
//
//    @Test
//    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
//        BeerDTO expectedFoundBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//
//        when(groupBeerRepository.findByName(expectedFoundBeerDTO.getName())).thenReturn(Optional.empty());
//
//        assertThrows(BeerNotFoundException.class, () -> groupBeerService.findByName(expectedFoundBeerDTO.getName()));
//
//    }
//
//    //ListAll
//    @Test
//    void whenListBeerIsCalledThenReturnAListOfBeers() {
//        BeerDTO beerDTOListExpected = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers groupBeersListExpected = beerMapper.toModel(beerDTOListExpected);
//
//        when(groupBeerRepository.findAll()).thenReturn(Collections.singletonList(groupBeersListExpected));
//
//        List<BeerDTO> listBeers = groupBeerService.listAll();
//
//        assertThat(listBeers, is(not(empty())));
//        assertThat(listBeers.get(0), is(equalTo(beerDTOListExpected)));
//    }
//
//    //delete
//
//    @Test
//    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws BeerNotFoundException {
//        BeerDTO beerDTOForBerDeleted = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers beerforBeDeleted = beerMapper.toModel(beerDTOForBerDeleted);
//
//        when(groupBeerRepository.findById(beerDTOForBerDeleted.getId())).thenReturn(Optional.of(beerforBeDeleted));
//        doNothing().when(groupBeerRepository).deleteById(beerDTOForBerDeleted.getId());
//
//        groupBeerService.deleteById(beerDTOForBerDeleted.getId());
//
//        verify(groupBeerRepository, times(1)).findById(beerDTOForBerDeleted.getId());
//        verify(groupBeerRepository, times(1)).deleteById(beerDTOForBerDeleted.getId());
//    }
//
//    //increment
//
//    @Test
//    void whenIncrementIsCalledThenIncrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        //given
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);
//
//        //when
//        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
//        when(groupBeerRepository.save(expectedGroupBeers)).thenReturn(expectedGroupBeers);
//
//        int quantityToIncrement = 10;
//        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantity() + quantityToIncrement;
//
//        // then
//        BeerDTO incrementedBeerDTO = groupBeerService.increment(expectedBeerDTO.getId(), quantityToIncrement);
//
//        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDTO.getQuantity()));
//        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
//    }
//
//    @Test
//    void whenIncrementIsGreatherThanMaxThenThrowException() {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);
//
//        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
//
//        int quantityToIncrement = 80;
//        assertThrows(BeerStockExceededException.class, () -> groupBeerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
//    }
//
//    @Test
//    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);
//
//        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
//
//        int quantityToIncrement = 45;
//        assertThrows(BeerStockExceededException.class, () -> groupBeerService.increment(expectedBeerDTO.getId(), quantityToIncrement));
//    }
//
//    @Test
//    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToIncrement = 10;
//
//        when(groupBeerRepository.findById(INVALID_BEER_ID))
//                .thenReturn(Optional.empty());
//
//        assertThrows(BeerNotFoundException.class, () -> groupBeerService.increment(INVALID_BEER_ID, quantityToIncrement));
//    }
//
//
//
//
//    @Test
//    void whenDecrementIsCalledThenDecrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);
//
//        when(groupBeerRepository.findById(expectedBeerDTO.getId()))
//                .thenReturn(Optional.of(expectedGroupBeers));
//
//        when(groupBeerRepository.save(expectedGroupBeers))
//                .thenReturn(expectedGroupBeers);
//
//        int quantityToDecrement = 5;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO decrementedBeerDTO = groupBeerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(decrementedBeerDTO.getQuantity()));
//        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
//    }
//
//    //decrement
//
//    @Test
//    void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);
//
//        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
//        when(groupBeerRepository.save(expectedGroupBeers)).thenReturn(expectedGroupBeers);
//
//        int quantityToDecrement = 10;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO incrementedBeerDTO = groupBeerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(0));
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//    }
//
//    @Test
//    void whenDecrementIsLowerThanZeroThenThrowException() {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        GroupBeers expectedGroupBeers = beerMapper.toModel(expectedBeerDTO);
//
//        when(groupBeerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedGroupBeers));
//
//        int quantityToDecrement = 80;
//        assertThrows(BeerStockExceededException.class, () -> groupBeerService.decrement(expectedBeerDTO.getId(), quantityToDecrement));
//    }
//
//    @Test
//    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToDecrement = 10;
//
//        when(groupBeerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
//
//        assertThrows(BeerNotFoundException.class, () -> groupBeerService.decrement(INVALID_BEER_ID, quantityToDecrement));
//    }

}