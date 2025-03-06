package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.mapper.ClientMapper;
import com.javaacademy.flat_rent.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientDto save(ClientDto clientDto) {
        Client client = clientRepository.save(clientMapper.toEntity(clientDto));
        log.info("Клиент сохранен.");
        return clientMapper.toDto(client);
    }
    @Transactional
    public void delete(Integer id) {
        clientRepository.deleteById(id);
        log.info("Клиент удален.");
    }
}
