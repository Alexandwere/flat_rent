package com.javaacademy.flat_rent.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Регистрация бронирования")
public class BookingDto {
    @Schema(description = "id", examples = {"null", "1"}, nullable = true)
    private Integer id;

    @Schema(description = "клиент")
    @JsonProperty("client")
    @NonNull
    private ClientDto clientDto;

    @Schema(description = "id объявления", example = "2")
    @JsonProperty("advert_id")
    @NonNull
    private Integer advertId;

    @Schema(description = "дата начала", example = "2025-01-01")
    @JsonProperty("date_start")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate dateStart;

    @Schema(description = "дата окончания", example = "2025-01-10")
    @JsonProperty("date_finish")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate dateFinish;
}
