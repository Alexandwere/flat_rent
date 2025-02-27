package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Integer id;
    @JsonProperty("client_id")
    private Integer clientId;
    @JsonProperty("advert_id")
    private Integer advertId;
    @JsonProperty("date_start")
    private LocalDateTime dateStart;
    @JsonProperty("date_finish")
    private LocalDateTime dateFinish;
}
