package com.cervejaria.gerenciamento.cervejaria.mapper;


import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.dto.ProvideDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import com.cervejaria.gerenciamento.cervejaria.entity.Provide;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProvideMapper {

    ProvideMapper INSTANCE = Mappers.getMapper(ProvideMapper.class);

    Provide toModel(ProvideDTO provideDTO);

    ProvideDTO toDTO(Provide provide);
}
