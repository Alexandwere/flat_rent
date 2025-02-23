package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.entity.Apartment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApartmentMapper {

    Apartment toEntity(ApartmentDto apartmentDto);

    ApartmentDto toDto(Apartment apartment);
}
