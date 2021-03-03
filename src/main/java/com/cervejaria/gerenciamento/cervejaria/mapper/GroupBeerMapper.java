package com.cervejaria.gerenciamento.cervejaria.mapper;


import com.cervejaria.gerenciamento.cervejaria.dto.GroupBeerDTO;
import com.cervejaria.gerenciamento.cervejaria.entity.GroupBeers;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupBeerMapper {

    GroupBeerMapper INSTANCE = Mappers.getMapper(GroupBeerMapper.class);

    GroupBeers toBeerModel(GroupBeerDTO groupBeerDTO);

    GroupBeerDTO toBeerDTO(GroupBeers groupBeers);
}
