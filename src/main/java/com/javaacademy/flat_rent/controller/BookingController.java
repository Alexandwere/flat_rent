package com.javaacademy.flat_rent.controller;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/booking")
@RestController
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoRs save(BookingDto bookingDto) {
        return bookingService.save(bookingDto);
    }

    @GetMapping
    public Page<BookingDtoRs> findAllByEmail(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam String email) {
        return bookingService.findAllByEmail(pageNumber, email);
    }

}
