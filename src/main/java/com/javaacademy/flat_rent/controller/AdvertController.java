package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.service.AdvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/advert")
@RequiredArgsConstructor
@RestController
public class AdvertController {
    private final AdvertService advertService;

    @PostMapping
    public AdvertDtoRs save(AdvertDto advertDto) {
        return advertService.save(advertDto);
    }

    @GetMapping
    public Page<AdvertDtoRs> findAllByCity(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam String city) {
        return advertService.findAllByCity(pageNumber, city);
    }
}
