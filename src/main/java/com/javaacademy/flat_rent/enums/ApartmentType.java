package com.javaacademy.flat_rent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApartmentType {
    ONLY_ROOM("Только комната"),
    ONE_ROOM("1 комната"),
    TWO_ROOMS("2 комнаты"),
    THREE_ROOMS("3 комнаты"),
    FOUR_ROOMS("4 комнаты"),
    FOUR_PLUS_ROOMS("4 и больше комнат");

    private final String description;
}
