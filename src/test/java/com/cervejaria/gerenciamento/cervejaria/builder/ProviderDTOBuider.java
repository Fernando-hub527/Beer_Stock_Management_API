package com.cervejaria.gerenciamento.cervejaria.builder;

import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import lombok.Builder;

@Builder
public class ProviderDTOBuider {

    @Builder.Default
    private Long cnpj = 198937463L;

    @Builder.Default
    private Long telephone = 77998574669L;

    @Builder.Default
    private String name = "Distribuidora Beers";

    @Builder.Default
    private String address = "Planalto Ba";

    public ProvideDTO toProviderDTO() {
        return new ProvideDTO(cnpj,
                telephone,
                name,
                address);
    }

}
