package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.dto.GroupBeerDTO;
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

    public GroupBeerDTO createBeer(GroupBeerDTO groupBeerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(groupBeerDTO.getName());
        GroupBeers groupBeers = beerMapper.toBeerModel(groupBeerDTO);
        GroupBeers savedGroupBeers = groupBeerRepository.save(groupBeers);

        return beerMapper.toBeerDTO(savedGroupBeers);
    }

    public GroupBeerDTO findByName(String name) throws BeerNotFoundException {
        GroupBeers foundGroupBeers = groupBeerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));
        return beerMapper.toBeerDTO(foundGroupBeers);
    }

    public List<GroupBeerDTO> listAll() {
        return groupBeerRepository.findAll()
                .stream()
                .map(beerMapper::toBeerDTO)
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

    public GroupBeerDTO increment(Long id, int quantityToIncrement) throws BeerNotFoundException, BeerStockExceededException {
        GroupBeers groupBeersToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + groupBeersToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= groupBeersToIncrementStock.getMax()) {
            groupBeersToIncrementStock.setQuantity(groupBeersToIncrementStock.getQuantity() + quantityToIncrement);
            GroupBeers incrementedGroupBeersStock = groupBeerRepository.save(groupBeersToIncrementStock);
            return beerMapper.toBeerDTO(incrementedGroupBeersStock);
        }
        throw new BeerStockExceededException(id, quantityToIncrement);
    }

    public GroupBeerDTO decrement(Long id, Integer quantityForDecrement) throws BeerNotFoundException, BeerStockExceededException {
        GroupBeers groupBeersToDecrementStock = verifyIfExists(id);
        int quantityAfterDencrement = groupBeersToDecrementStock.getQuantity() - quantityForDecrement;

        if (quantityAfterDencrement >= 0){
            groupBeersToDecrementStock.setQuantity(quantityAfterDencrement);
            GroupBeerDTO DecrementedBeerStock = beerMapper.toBeerDTO(groupBeerRepository.save(groupBeersToDecrementStock));
            return DecrementedBeerStock;
        }

        throw new BeerStockExceededException(id, quantityForDecrement);
    }
}











