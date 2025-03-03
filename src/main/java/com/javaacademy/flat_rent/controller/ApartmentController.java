package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/apartment")
@RestController
public class ApartmentController {

    private final ApartmentService apartmentService;

    @PostMapping
    public ApartmentDto save(@RequestBody ApartmentDto apartmentDto) {
        return apartmentService.save(apartmentDto);
    }
}
