package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.exception.AdvertNotExistsException;
import com.javaacademy.flat_rent.exception.ClientNotExistsException;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BookingMapper {
    @Autowired
    AdvertRepository advertRepository;
    @Autowired
    ClientRepository clientRepository;

    @Mapping(target = "advert", source = "advertId", qualifiedByName = "getAdvert")
    @Mapping(target = "client", source = "clientId", qualifiedByName = "getClient")
    public abstract Booking toEntityWithRelation(BookingDto bookingDto);

    public abstract BookingDtoRs toDto(Booking booking);

    @Named("getAdvert")
    protected Advert getAdvert(Integer id) {
        return advertRepository.findById(id)
                .orElseThrow(() -> new AdvertNotExistsException("Объявлений с таким ID не существует."));
    }

    @Named("getClient")
    protected Client getClient(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotExistsException("Клиента с таким ID не существует."));
    }
}
