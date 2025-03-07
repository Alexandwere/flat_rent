package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.flat_rent.enums.ApartmentType;
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
@Schema(description = "Апартаменты")
public class ApartmentDto {
    @Schema(description = "id", examples = {"null", "1"}, nullable = true)
    private Integer id;

    @Schema(description = "город", example = "Moscow")
    @NonNull
    private String city;

    @Schema(description = "улица", example = "Хлебная")
    @NonNull
    private String street;

    @Schema(description = "дом", example = "1Б")
    @NonNull
    private String house;

    @Schema(description = "тип апартаментов", example = "TWO_ROOMS")
    @JsonProperty("apartment_type")
    @NonNull
    private ApartmentType apartmentType;
}
