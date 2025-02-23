package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertDto {
    private Integer id;
    private BigDecimal price;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("apartment_id")
    private Integer apartmentId;
    private String description;
}
