package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Бронирование")
public class BookingDtoRs {
    @Schema(description = "id", example = "1")
    private Integer id;

    @Schema(description = "клиент")
    private ClientDto client;

    @Schema(description = "объявление")
    private AdvertDtoRs advert;

    @Schema(description = "дата начала", example = "2025-01-01")
    @JsonProperty("date_start")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStart;

    @Schema(description = "дата окончания", example = "2025-01-10")
    @JsonProperty("date_finish")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFinish;

    @Schema(description = "итоговая цена", example = "1000")
    @JsonProperty("result_price")
    private BigDecimal resultPrice;
}
