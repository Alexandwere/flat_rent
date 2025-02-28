package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdvertDtoRs {
    private Integer id;

    private BigDecimal price;

    @JsonProperty("is_active")
    private Boolean isActive;

    private ApartmentDto apartment;

    private String description;
}
