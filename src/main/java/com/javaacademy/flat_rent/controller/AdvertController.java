package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.service.AdvertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(
        name = "Контроллер для работы с объявлениями",
        description = "Содержит команды для совершения действий с объявлениями"
)
@RequestMapping("/advert")
@RequiredArgsConstructor
@RestController
public class AdvertController {
    private final AdvertService advertService;

    @Operation(summary = "Сохранение объявления",
            description = "Сохранения объявления с ценой, ID апартаментов, статусом объявления и описанием.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AdvertDtoRs save(@RequestBody AdvertDto advertDto) {
        return advertService.save(advertDto);
    }

    @Operation(summary = "Поиск всех объявлений по городу",
            description = "Поиск всех объявлений по городу.")
    @GetMapping
    public Page<AdvertDtoRs> findAllByCity(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam String city) {
        return advertService.findAllByCity(pageNumber, city);
    }
}
