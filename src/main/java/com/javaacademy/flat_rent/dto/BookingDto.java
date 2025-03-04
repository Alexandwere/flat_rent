package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingDto {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "клиент")
    @JsonProperty("client")
    private ClientDto clientDto;

    @Schema(description = "id объявления")
    @JsonProperty("advert_id")
    private Integer advertId;

    @Schema(description = "дата начала")
    @JsonProperty("date_start")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStart;

    @Schema(description = "дата окончания")
    @JsonProperty("date_finish")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFinish;
}
