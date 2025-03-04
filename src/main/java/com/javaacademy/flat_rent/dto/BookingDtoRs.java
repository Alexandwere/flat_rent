package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingDtoRs {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "клиент")
    private ClientDto client;

    @Schema(description = "объявление")
    private AdvertDtoRs advert;

    @Schema(description = "дата начала")
    @JsonProperty("date_start")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStart;

    @Schema(description = "дата окончания")
    @JsonProperty("date_finish")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFinish;

    @Schema(description = "итоговая цена")
    @JsonProperty("result_price")
    private BigDecimal resultPrice;
}
