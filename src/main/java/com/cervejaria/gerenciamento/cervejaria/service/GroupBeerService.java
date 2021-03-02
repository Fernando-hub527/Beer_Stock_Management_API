package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerStockExceededException;
import com.cervejaria.gerenciamento.cervejaria.mapper.BeerMapper;
import com.cervejaria.gerenciamento.cervejaria.repository.GroupBeerRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GroupBeerService {

    private final GroupBeerRepository groupBeerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        GroupBeers groupBeers = beerMapper.toModel(beerDTO);
        GroupBeers savedGroupBeers = groupBeerRepository.save(groupBeers);
        return beerMapper.toDTO(savedGroupBeers);
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        GroupBeers foundGroupBeers = groupBeerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toDTO(foundGroupBeers);
    }

    public List<BeerDTO> listAll() {
        return groupBeerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        groupBeerRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<GroupBeers> optSavedBeer = groupBeerRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    private GroupBeers verifyIfExists(Long id) throws BeerNotFoundException {
        return groupBeerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }

    public BeerDTO increment(Long id, int quantityToIncrement) throws BeerNotFoundException, BeerStockExceededException {
        GroupBeers groupBeersToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + groupBeersToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= groupBeersToIncrementStock.getMax()) {
            groupBeersToIncrementStock.setQuantity(groupBeersToIncrementStock.getQuantity() + quantityToIncrement);
            GroupBeers incrementedGroupBeersStock = groupBeerRepository.save(groupBeersToIncrementStock);
            return beerMapper.toDTO(incrementedGroupBeersStock);
        }
        throw new BeerStockExceededException(id, quantityToIncrement);
    }

    public BeerDTO decrement(Long id, Integer quantityForDecrement) throws BeerNotFoundException, BeerStockExceededException {
        GroupBeers groupBeersToDecrementStock = verifyIfExists(id);
        int quantityAfterDencrement = groupBeersToDecrementStock.getQuantity() - quantityForDecrement;

        if (quantityAfterDencrement >= 0){
            groupBeersToDecrementStock.setQuantity(quantityAfterDencrement);
            BeerDTO DecrementedBeerStock = beerMapper.toDTO(groupBeerRepository.save(groupBeersToDecrementStock));
            return DecrementedBeerStock;
        }

        throw new BeerStockExceededException(id, quantityForDecrement);
    }
}











