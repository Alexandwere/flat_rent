package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.javaacademy.flat_rent.enums.ApartmentType;
import lombok.Data;

@Data
public class ApartmentDto {
    private Integer id;

    private String city;

    private String street;

    private String house;

    @JsonProperty("apartment_type")
    private ApartmentType apartmentType;
}
