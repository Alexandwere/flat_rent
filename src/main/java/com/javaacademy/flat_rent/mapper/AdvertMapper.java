package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.exception.ApartmentNotExistsException;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AdvertMapper {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Mapping(target = "apartment", source = "apartmentId", qualifiedByName = "getApartment")
    public abstract Advert toEntityWithRelation(AdvertDto advertDto);

    public abstract AdvertDtoRs toDto(Advert advert);

    @Named("getApartment")
    protected Apartment getApartment(Integer id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new ApartmentNotExistsException("Квартир с таким ID (%s) не существует."
                        .formatted(id)));
    }
}
