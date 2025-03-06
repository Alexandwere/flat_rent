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
@Schema(description = "Регистрация объявления")
public class AdvertDto {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "цена")
    private BigDecimal price;

    @Schema(description = "активность")
    @JsonProperty("is_active")
    private Boolean isActive;

    @Schema(description = "id апартаментов")
    @JsonProperty("apartment_id")
    private Integer apartmentId;

    @Schema(description = "описание")
    private String description;
}
