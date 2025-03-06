package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.mapper.AdvertMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertService {
    private static final int PAGE_SIZE = 10;

    private final AdvertRepository advertRepository;
    private final AdvertMapper advertMapper;

    @Transactional
    public AdvertDtoRs save(AdvertDto advertDto) {
        Advert advert = advertRepository.save(advertMapper.toEntityWithRelation(advertDto));
        log.info("Объявление сохранено");
        return advertMapper.toDto(advert);
    }

    @Transactional(readOnly = true)
    public Page<AdvertDtoRs> findAllByCity(Integer pageNumber, String city) {
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        Page<Advert> adverts = advertRepository.findAllByCity(city, pageRequest);
        log.info("Выполнен поиск по городу.");
        return adverts.map(advertMapper::toDto);
    }

}
