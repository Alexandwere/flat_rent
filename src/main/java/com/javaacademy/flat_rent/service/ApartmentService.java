package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.exception.FilledIdException;
import com.javaacademy.flat_rent.mapper.ApartmentMapper;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentMapper apartmentMapper;
    private final ApartmentRepository apartmentRepository;

    @Transactional
    public ApartmentDto save(ApartmentDto apartmentDto) {
        if (apartmentDto.getId() != null) {
            throw new FilledIdException("ID апартаментов должен быть null");
        }
        Apartment apartment = apartmentRepository.save(apartmentMapper.toEntity(apartmentDto));
        log.trace("Апартаменты сохранены.");
        return apartmentMapper.toDto(apartment);
    }
}
