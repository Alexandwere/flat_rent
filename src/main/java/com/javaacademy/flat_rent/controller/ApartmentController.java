package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.service.ApartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Контроллер для работы с апартаментами",
        description = "Содержит команды для совершения действий с апартаментами"
)
@RequiredArgsConstructor
@RequestMapping("/apartment")
@RestController
public class ApartmentController {

    private final ApartmentService apartmentService;

    @Operation(summary = "Сохранение апартаментов",
            description = "Сохранения апартаментов по городу, улице, номеру дома и типа апартаментов.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApartmentDto save(@RequestBody ApartmentDto apartmentDto) {
        return apartmentService.save(apartmentDto);
    }
}
