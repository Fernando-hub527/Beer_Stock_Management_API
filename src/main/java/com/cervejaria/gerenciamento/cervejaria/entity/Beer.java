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

    @ManyToOne
    @JoinColumn(name = "cnpj_provide", referencedColumnName = "cnpj", nullable = false)
    private Provide provide;

    @ManyToOne
    @JoinColumn(name = "id_Beers",  referencedColumnName = "id", nullable = false)
    private GroupBeers groupBeers;

}
