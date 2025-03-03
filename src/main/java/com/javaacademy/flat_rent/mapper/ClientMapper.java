package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = BookingMapper.class)
public interface ClientMapper {

    @Mapping(target = "bookings", ignore = true)
    Client toEntity(ClientDto clientDto);

    ClientDto toDto(Client client);
}
