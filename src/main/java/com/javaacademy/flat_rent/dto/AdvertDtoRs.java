package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объявление")
public class AdvertDtoRs {
    @Schema(description = "id", example = "1")
    private Integer id;

    @Schema(description = "цена", example = "100")
    private BigDecimal price;

    @Schema(description = "активность", example = "true")
    @JsonProperty("is_active")
    private Boolean isActive;

    @Schema(description = "апартаменты")
    private ApartmentDto apartment;

    @Schema(description = "описание", example = "Апартаменты в центре города.")
    private String description;
}
