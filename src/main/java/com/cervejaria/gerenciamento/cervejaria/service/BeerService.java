package com.cervejaria.gerenciamento.cervejaria.service;

import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.GroupBeerDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.Beer;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.mapper.BeerMapper;
import com.cervejaria.gerenciamento.cervejaria.mapper.GroupBeerMapper;
import com.cervejaria.gerenciamento.cervejaria.repository.BeerRepository;
import com.cervejaria.gerenciamento.cervejaria.repository.GroupBeerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public void createBeer(List<BeerDTO> beerDTOList) throws BeerAlreadyRegisteredException {
        List<Beer> beerDTOListForSave = new ArrayList<>();
        for (BeerDTO beerDTO : beerDTOList) {
            verifyIfIsAlreadyRegistered(beerDTO.getCodeBarra());
            beerDTOListForSave.add(beerMapper.toModel(beerDTO));
        }
        beerDTOListForSave.forEach(beerRepository::save);

    }

//    public GroupBeerDTO findByName(String name) throws BeerNotFoundException {
//        GroupBeers foundGroupBeers = groupBeerRepository.findByName(name)
//                .orElseThrow(() -> new BeerNotFoundException(name));
//        return groupBeerMapper.toBeerDTO(foundGroupBeers);
//    }
//
//    public List<GroupBeerDTO> listAll() {
//        return groupBeerRepository.findAll()
//                .stream()
//                .map(groupBeerMapper::toBeerDTO)
//                .collect(Collectors.toList());
//    }
//
//    public void deleteById(Long id) throws BeerNotFoundException {
//        verifyIfExists(id);
//        groupBeerRepository.deleteById(id);
//    }

    private void verifyIfIsAlreadyRegistered(Long id) throws BeerAlreadyRegisteredException {
        Optional<Beer> beerFound = beerRepository.findById(id);
        if (beerFound.isPresent()) {
            throw new BeerAlreadyRegisteredException(id);
        }
    }

//    private GroupBeers verifyIfExists(Long id) throws BeerNotFoundException {
//        return groupBeerRepository.findById(id)
//                .orElseThrow(() -> new BeerNotFoundException(id));
//    }
//
//    public GroupBeerDTO increment(Long id, int quantityToIncrement) throws BeerNotFoundException, BeerStockExceededException {
//        GroupBeers groupBeersToIncrementStock = verifyIfExists(id);
//        int quantityAfterIncrement = quantityToIncrement + groupBeersToIncrementStock.getQuantity();
//        if (quantityAfterIncrement <= groupBeersToIncrementStock.getMax()) {
//            groupBeersToIncrementStock.setQuantity(groupBeersToIncrementStock.getQuantity() + quantityToIncrement);
//            GroupBeers incrementedGroupBeersStock = groupBeerRepository.save(groupBeersToIncrementStock);
//            return groupBeerMapper.toBeerDTO(incrementedGroupBeersStock);
//        }
//        throw new BeerStockExceededException(id, quantityToIncrement);
//    }
//
//    public GroupBeerDTO decrement(Long id, Integer quantityForDecrement) throws BeerNotFoundException, BeerStockExceededException {
//        GroupBeers groupBeersToDecrementStock = verifyIfExists(id);
//        int quantityAfterDencrement = groupBeersToDecrementStock.getQuantity() - quantityForDecrement;
//
//        if (quantityAfterDencrement >= 0){
//            groupBeersToDecrementStock.setQuantity(quantityAfterDencrement);
//            GroupBeerDTO DecrementedBeerStock = groupBeerMapper.toBeerDTO(groupBeerRepository.save(groupBeersToDecrementStock));
//            return DecrementedBeerStock;
//        }
//
//        throw new BeerStockExceededException(id, quantityForDecrement);
//    }
}
