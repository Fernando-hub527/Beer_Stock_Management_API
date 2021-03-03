package com.cervejaria.gerenciamento.cervejaria.repository;

import com.cervejaria.gerenciamento.cervejaria.entity.Beer;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeerRepository extends JpaRepository<Beer, Long> {
    //Optional<Beer> findByCodeBarra(String name);
}
