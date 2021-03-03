package com.cervejaria.gerenciamento.cervejaria.dto;

import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.entity.Provide;
import com.cervejaria.gerenciamento.cervejaria.enums.BeerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerDTO {

    private Long codeBarra;

    @NotNull
    @Size(min = 1, max = 200)
    private String date;

    @NotNull
    private Long custo;


    @NotNull
    @Max(500)
    private Provide provide;

    @NotNull
    @Max(100)
    private GroupBeers groupBeers;

}
