package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @DeleteMapping("/{id}")
    public void deleteClientCascade(@NonNull Integer id) {
        clientService.delete(id);
    }
}
