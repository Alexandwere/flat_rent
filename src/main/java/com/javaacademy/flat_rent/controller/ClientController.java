package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Контроллер для работы с клиентами",
        description = "Содержит команды для совершения действий с клиентами"
)
@RequiredArgsConstructor
@RequestMapping("/client")
@RestController
public class ClientController {
    private final ClientService clientService;

    @Operation(summary = "Удаление клиента и всех его бронирований",
            description = "Удаление клиента и всех бронирований, связанных с ним.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное удаление клиента и его бронирований.")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteClientCascade(@PathVariable @NonNull Integer id) {
        clientService.delete(id);
    }
}
