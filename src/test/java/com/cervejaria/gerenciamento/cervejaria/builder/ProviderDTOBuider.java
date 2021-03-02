package com.cervejaria.gerenciamento.cervejaria.builder;

import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import lombok.Builder;

@Builder
public class ProviderDTOBuider {

    @Builder.Default
    private String cnpj = "198937463";

    @Builder.Default
    private String telephone = "77998574669";

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
