package com.javaacademy.flat_rent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Клиент")
public class ClientDto {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "имя")
    private String name;

    @Schema(description = "эл. почта")
    private String email;
}
