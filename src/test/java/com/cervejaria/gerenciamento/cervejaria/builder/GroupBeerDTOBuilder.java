package com.cervejaria.gerenciamento.cervejaria.builder;

import com.cervejaria.gerenciamento.cervejaria.dto.GroupBeerDTO;
import com.cervejaria.gerenciamento.cervejaria.enums.BeerType;
import lombok.Builder;


@Builder
public class GroupBeerDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Brahma";

    @Builder.Default
    private String brand = "Ambev";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private BeerType type = BeerType.LAGER;

    public GroupBeerDTO toBeerDTO() {
        return new GroupBeerDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }
}
