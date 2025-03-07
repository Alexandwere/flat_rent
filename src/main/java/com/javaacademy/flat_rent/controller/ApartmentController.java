package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.service.ApartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            description = "Сохранения апартаментов по городу, улице, номеру дома и типа апартаментов. "
                    + "Возможные типы: ONLY_ROOM, ONE_ROOM, TWO_ROOMS, THREE_ROOMS, FOUR_AND_MORE_ROOMS.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Успешное сохранение.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApartmentDto.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApartmentDto save(@RequestBody ApartmentDto apartmentDto) {
        return apartmentService.save(apartmentDto);
    }
}
