package com.javaacademy.flat_rent.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {
    private Integer id;

    private String name;

    private String email;
}
