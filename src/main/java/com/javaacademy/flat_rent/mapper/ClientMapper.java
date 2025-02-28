package com.javaacademy.flat_rent.mapper;

import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = BookingMapper.class)
public interface ClientMapper {

    Client toEntity(ClientDto clientDto);

    ClientDto toDto(Client client);
}
