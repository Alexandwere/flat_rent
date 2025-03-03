package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.mapper.AdvertMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertService {

    private final AdvertRepository advertRepository;
    private final AdvertMapper advertMapper;

    public AdvertDtoRs save(AdvertDto advertDto) {
        Advert advert = advertRepository.save(advertMapper.toEntityWithRelation(advertDto));
        return advertMapper.toDto(advert);
    }

}
