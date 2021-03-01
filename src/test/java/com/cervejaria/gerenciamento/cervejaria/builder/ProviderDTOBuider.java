package com.cervejaria.gerenciamento.cervejaria.builder;

import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import lombok.Builder;

@Builder
public class ProviderDTOBuider {

    @Builder.Default
    private Integer CNPJ = 198937463;

    @Builder.Default
    private String name = "DistribuidoraBeers";

    @Builder.Default
    private Long telephone = 7799857399L;

    @Builder.Default
    private String address = "Planalto-Ba / Rua Diocleciano 75";

    public ProvideDTO toProviderDTO() {
        return new ProvideDTO(CNPJ,
                name,
                telephone,
                address);
    }

}
