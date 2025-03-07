package com.javaacademy.flat_rent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Клиент")
public class ClientDto {
    @Schema(description = "id", examples = {"null", "1"}, nullable = true)
    private Integer id;

    @Schema(description = "имя", example = "Ирина")
    @NonNull
    private String name;

    @Schema(description = "эл. почта", example = "irina@mail.ru")
    @NonNull
    private String email;
}
