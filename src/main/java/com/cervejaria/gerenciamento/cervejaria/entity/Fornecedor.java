package com.cervejaria.gerenciamento.cervejaria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    private Integer CNPJ;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer telefone;

    @Column(nullable = false)
    private String endereco;
}
