package com.cervejaria.gerenciamento.cervejaria.mapper;


import com.cervejaria.gerenciamento.cervejaria.dto.BeerDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    GroupBeers toModel(BeerDTO beerDTO);

    BeerDTO toDTO(GroupBeers groupBeers);
}
