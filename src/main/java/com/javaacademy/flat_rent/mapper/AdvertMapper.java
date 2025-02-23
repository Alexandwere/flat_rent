package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.exception.NotFoundApartmentException;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AdvertMapper {
    @Autowired
    ApartmentRepository apartmentRepository;

    @Mapping(target = "apartment", source = "apartmentId", qualifiedByName = "getApartment")
    public abstract Advert toEntity(AdvertDto advertDto);

    @Mapping(target = "apartmentDto", source = "apartment")
    public abstract AdvertDtoRs toDto(Advert advert);

    @Named("getApartment")
    protected Apartment getApartment(Integer id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundApartmentException("Квартир с таким ID не существует."));
    }
}
