package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingDtoRs {
    private Integer id;

    private ClientDto client;

    private AdvertDtoRs advert;

    @JsonProperty("date_start")
    private LocalDate dateStart;

    @JsonProperty("date_finish")
    private LocalDate dateFinish;

    @JsonProperty("result_price")
    private BigDecimal resultPrice;
}
