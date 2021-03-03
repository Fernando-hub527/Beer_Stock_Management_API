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
public class Provide {

    @Id
    private String cnpj;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

}
