package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.builder.ProviderDTOBuider;
import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.Provide;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.mapper.ProvideMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {


    @Mock
    private ProvideRepository provideRepository;
    private ProvideMapper provideMapper = ProvideMapper.INSTANCE;

    @InjectMocks
    private ProviderService providerService;


    @Test
    void whenCreateProviderIsCalledThenProviderIsCreate() throws BeerAlreadyRegisteredException {
        //given
        ProvideDTO provideDTOExpected = ProviderDTOBuider.builder().build().toProviderDTO();
        Provide provideForSave = provideMapper.toProvideModel(provideDTOExpected);

        //when
        when(provideRepository.findByName(provideDTOExpected.getName())).thenReturn(Optional.empty());
        when(provideRepository.save(provideForSave)).thenReturn(provideForSave);

        //then
        ProvideDTO provideDTOsaved= providerService.createProvider(provideDTOExpected);

        assertThat(provideDTOsaved.getName(), is(equalTo(provideDTOExpected.getName())));

    }

    @Test
    void whenAlreadyRegisteredProviderInformedThenAnExceptionShouldBeThrown() throws BeerAlreadyRegisteredException {
        ProvideDTO providerDTOExpected = ProviderDTOBuider.builder().build().toProviderDTO();
        Provide duplicateGroupBeers = provideMapper.toProvideModel(providerDTOExpected);

        //simula a existencia do registro inserido
        when(provideRepository.findByName(providerDTOExpected.getName())).thenReturn(Optional.of(duplicateGroupBeers));

        assertThrows(BeerAlreadyRegisteredException.class, () -> providerService.createProvider(providerDTOExpected));
    }

    //findByName
    @Test
    void whenValidProviderNameIsGivenThenReturnAProvider() throws BeerNotFoundException {
        ProvideDTO providerDTOExpected = ProviderDTOBuider.builder().build().toProviderDTO();
        Provide providerExpected = provideMapper.toProvideModel(providerDTOExpected);

        //força o retorno de um nome registrado
        when(provideRepository.findByName(providerExpected.getName())).thenReturn(Optional.of(providerExpected));

        ProvideDTO providerReturned = providerService.findByName(providerDTOExpected.getName());

        assertThat(providerReturned.getName(), is(equalTo(providerExpected.getName())));

    }

    @Test
    void whenNotRegisteredProviderNameIsGivenThenThrowAnException() {
        ProvideDTO expectedFoundProviderDTO = ProviderDTOBuider.builder().build().toProviderDTO();

        when(provideRepository.findByName(expectedFoundProviderDTO.getName())).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () -> providerService.findByName(expectedFoundProviderDTO.getName()));

    }

    //ListAll
    @Test
    void whenListProviderIsCalledThenReturnAListOfProvider() {
        ProvideDTO provideDTOListExpected = ProviderDTOBuider.builder().build().toProviderDTO();
        Provide provideListExpected = provideMapper.toProvideModel(provideDTOListExpected);

        when(provideRepository.findAll()).thenReturn(Collections.singletonList(provideListExpected));

        List<ProvideDTO> listBeers = providerService.listAll();

        assertThat(listBeers, is(not(empty())));
        assertThat(listBeers.get(0), is(equalTo(provideDTOListExpected)));
    }

    //delete

    @Test
    void whenExclusionIsCalledWithValidIdThenAProviderShouldBeDeleted() throws BeerNotFoundException {
        ProvideDTO beerDTOForBerDeleted = ProviderDTOBuider.builder().build().toProviderDTO();
        Provide beerforBeDeleted = provideMapper.toProvideModel(beerDTOForBerDeleted);

        when(provideRepository.findById(beerDTOForBerDeleted.getCnpj())).thenReturn(Optional.of(beerforBeDeleted));
        doNothing().when(provideRepository).deleteById(beerDTOForBerDeleted.getCnpj());

        providerService.deleteById(beerDTOForBerDeleted.getCnpj());

        verify(provideRepository, times(1)).findById(beerDTOForBerDeleted.getCnpj());
        verify(provideRepository, times(1)).deleteById(beerDTOForBerDeleted.getCnpj());
    }

    @Test
    void whenExclusionIsCalledWithoutValidIdThenAProviderShouldBeDeleted() throws BeerNotFoundException {
        ProvideDTO beerDTOForBerDeleted = ProviderDTOBuider.builder().build().toProviderDTO();
        Provide beerforBeDeleted = provideMapper.toProvideModel(beerDTOForBerDeleted);

        when(provideRepository.findById(beerDTOForBerDeleted.getCnpj())).thenReturn(Optional.empty());

        assertThrows(BeerNotFoundException.class, () ->providerService.deleteById(beerDTOForBerDeleted.getCnpj()));
    }

}