package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Контроллер для работы с бронированиями",
        description = "Содержит команды для совершения действий с бронированиями"
)
@RequiredArgsConstructor
@RequestMapping("/booking")
@RestController
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Сохранение бронирования",
            description = "Сохранение бронирования, на вход клиент, ID апартаментов, даты бронирования.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingDtoRs save(@RequestBody BookingDto bookingDto) {
        return bookingService.save(bookingDto);
    }

    @Operation(summary = "Поиск бронирований по email",
            description = "Поиск бронирований по email.")
    @GetMapping
    public Page<BookingDtoRs> findAllByEmail(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam String email) {
        return bookingService.findAllByEmail(pageNumber, email);
    }

}
