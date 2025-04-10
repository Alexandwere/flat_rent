package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Регистрация объявления")
public class AdvertDto {
    @Schema(description = "id", examples = {"null", "1"}, nullable = true)
    private Integer id;

    @Schema(description = "цена", example = "100")
    @NonNull
    private BigDecimal price;

    @Schema(description = "активность", example = "true")
    @JsonProperty("is_active")
    @NonNull
    private Boolean isActive;

    @Schema(description = "id апартаментов")
    @JsonProperty("apartment_id")
    @NonNull
    private Integer apartmentId;

    @Schema(description = "описание", example = "Апартаменты в центре города.")
    @NonNull
    private String description;
}
