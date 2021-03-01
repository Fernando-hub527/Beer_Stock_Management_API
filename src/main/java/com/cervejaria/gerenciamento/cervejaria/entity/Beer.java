package com.cervejaria.gerenciamento.cervejaria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    private Long codeBarra;

    @Column(nullable = false)
    private Long custo;

    @Column(nullable = false)
    private String date;

//    @ManyToOne
//    @JoinColumn(name = "fornecedores", referencedColumnName = "CNPJ", nullable = false)
//    private Provide cnpjProvide;

    @ManyToOne
    @JoinColumn(name = "id_Beers",  referencedColumnName = "id", nullable = false)
    private GroupBeers groupBeers;

}
