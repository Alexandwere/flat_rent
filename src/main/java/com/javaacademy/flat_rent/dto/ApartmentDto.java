package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.flat_rent.enums.ApartmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApartmentDto {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "город")
    private String city;

    @Schema(description = "улица")
    private String street;

    @Schema(description = "дом")
    private String house;

    @Schema(description = "типа апартаментов")
    @JsonProperty("apartment_type")
    private ApartmentType apartmentType;
}
