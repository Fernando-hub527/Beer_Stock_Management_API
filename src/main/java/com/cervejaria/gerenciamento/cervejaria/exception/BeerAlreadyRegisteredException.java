package com.cervejaria.gerenciamento.cervejaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerAlreadyRegisteredException extends Exception{

    public BeerAlreadyRegisteredException(String beerName) {
        super(String.format("%s already registered in the system.", beerName));
    }

    public BeerAlreadyRegisteredException(Long id) {
        super(String.format("id %s already registered in the system.", id));
    }
}
