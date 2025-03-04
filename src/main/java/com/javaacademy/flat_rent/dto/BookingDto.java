package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingDto {
    private Integer id;

    @JsonProperty("client")
    private ClientDto clientDto;

    @JsonProperty("advert_id")
    private Integer advertId;

    @JsonProperty("date_start")
    private LocalDate dateStart;

    @JsonProperty("date_finish")
    private LocalDate dateFinish;
}
