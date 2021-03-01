package com.cervejaria.gerenciamento.cervejaria.repository;

import com.cervejaria.gerenciamento.cervejaria.entity.Fornecedor;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<GroupBeers> findByName(String name);

}
