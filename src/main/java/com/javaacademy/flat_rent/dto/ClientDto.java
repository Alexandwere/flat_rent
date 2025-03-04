package com.javaacademy.flat_rent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "имя")
    private String name;

    @Schema(description = "эл. почта")
    private String email;
}
