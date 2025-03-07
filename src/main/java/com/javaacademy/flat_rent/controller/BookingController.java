package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Успешное сохранение.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingDtoRs.class))),
            @ApiResponse(responseCode = "404", description = "Апартаменты не найдены.",
                    content = @Content(mediaType = "plain/text")),
            @ApiResponse(responseCode = "409", description = "Объявление неактивно.",
                    content = @Content(mediaType = "plain/text")),
            @ApiResponse(responseCode = "409", description = "Данные даты недоступны для бронирования.",
                    content = @Content(mediaType = "plain/text"))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingDtoRs save(@RequestBody BookingDto bookingDto) {
        return bookingService.save(bookingDto);
    }

    @Operation(summary = "Поиск бронирований по email",
            description = "Поиск бронирований по email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное поиск по email.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookingDtoRs.class)))
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<BookingDtoRs> findAllByEmail(
            @RequestParam(name = "номер страницы", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "эл.почта") String email) {
        return bookingService.findAllByEmail(pageNumber, email);
    }

}
