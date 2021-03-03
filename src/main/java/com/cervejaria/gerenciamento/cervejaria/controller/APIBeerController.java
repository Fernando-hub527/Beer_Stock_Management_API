package com.cervejaria.gerenciamento.cervejaria.controller;

import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.GroupBeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.QuantityDTO;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerAlreadyRegisteredException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerNotFoundException;
import com.cervejaria.gerenciamento.cervejaria.exception.BeerStockExceededException;
import com.cervejaria.gerenciamento.cervejaria.service.GroupBeerService;
import com.cervejaria.gerenciamento.cervejaria.service.ProviderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class APIBeerController implements APIBeerControllerDocs {

    private final GroupBeerService groupBeerService;
    private final ProviderService providerService;

    @PostMapping("/{beer}/group")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupBeerDTO createBeer(@RequestBody @Valid GroupBeerDTO groupBeerDTO, List<BeerDTO> beerDTOList) throws BeerAlreadyRegisteredException {
        return groupBeerService.createGroupBeer(groupBeerDTO, beerDTOList);
    }

    @PostMapping("/{provider}/provider")
    @ResponseStatus(HttpStatus.CREATED)
    public ProvideDTO createProvide(@RequestBody @Valid ProvideDTO provideDTO) throws BeerAlreadyRegisteredException {
        return providerService.createProvider(provideDTO);
    }


    @GetMapping("/{name}")
    public GroupBeerDTO findByName(@PathVariable String name) throws BeerNotFoundException {
        return groupBeerService.findByName(name);
    }

    @GetMapping
    public List<GroupBeerDTO> listBeers() {
        return groupBeerService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
        groupBeerService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public GroupBeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededException {
        return groupBeerService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    public GroupBeerDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededException {
        return groupBeerService.decrement(id, quantityDTO.getQuantity());
    }

}
