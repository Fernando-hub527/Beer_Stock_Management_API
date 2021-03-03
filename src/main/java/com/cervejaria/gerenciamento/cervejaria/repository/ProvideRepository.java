package com.cervejaria.gerenciamento.cervejaria.repository;

import com.cervejaria.gerenciamento.cervejaria.entity.Provide;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProvideRepository extends JpaRepository<Provide, String> {
    Optional<Provide> findByName(String name);

}
