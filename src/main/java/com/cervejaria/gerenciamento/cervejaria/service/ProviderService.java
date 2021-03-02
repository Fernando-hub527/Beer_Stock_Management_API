package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.entity.Provide;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.mapper.ProvideMapper;
import com.cervejaria.gerenciamento.cervejaria.repository.ProvideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProviderService {

    private final ProvideRepository provideRepository;
    private final ProvideMapper provideMapper = ProvideMapper.INSTANCE;

    public ProvideDTO createProvider(ProvideDTO provideDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(provideDTO.getName());
        Provide provide = provideMapper.toModel(provideDTO);
        Provide provideSave = provideRepository.save(provide);
        return provideMapper.toDTO(provideSave);
    }

    public ProvideDTO findByName(String name) throws BeerNotFoundException {
        Provide foundProvide = provideRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return provideMapper.toDTO(foundProvide);
    }

    public List<ProvideDTO> listAll() {
        return provideRepository.findAll()
                .stream()
                .map(provideMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        provideRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Provide> optSavedProvider = provideRepository.findByName(name);
        if (optSavedProvider.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    private Provide verifyIfExists(Long id) throws BeerNotFoundException {
        return provideRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }
}
