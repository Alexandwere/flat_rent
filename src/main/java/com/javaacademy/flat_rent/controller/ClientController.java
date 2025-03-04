package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.service.ClientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/client")
@RestController
public class ClientController {
    private final ClientService clientService;

    @DeleteMapping("/{id}")
    public void deleteClientCascade(@NonNull Integer id) {
        clientService.delete(id);
    }
}
