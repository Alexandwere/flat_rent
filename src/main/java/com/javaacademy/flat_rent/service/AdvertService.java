package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.mapper.AdvertMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertService {
    private static final int PAGE_SIZE = 10;

    private final AdvertRepository advertRepository;
    private final AdvertMapper advertMapper;

    public AdvertDtoRs save(AdvertDto advertDto) {
        Advert advert = advertRepository.save(advertMapper.toEntityWithRelation(advertDto));
        return advertMapper.toDto(advert);
    }

    public Page<AdvertDtoRs> findAllByCity(int pageNumber, String city) {
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        Page<Advert> adverts = advertRepository.findAllByCity(city, pageRequest);
        return adverts.map(advertMapper::toDto);
    }

}
