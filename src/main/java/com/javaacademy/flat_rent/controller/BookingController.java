package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoRs save(BookingDto bookingDto) {
        return bookingService.save(bookingDto);
    }

}
