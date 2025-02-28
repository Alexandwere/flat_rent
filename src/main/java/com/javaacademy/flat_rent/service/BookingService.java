package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.mapper.BookingMapper;
import com.javaacademy.flat_rent.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import static java.math.BigDecimal.valueOf;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingDtoRs save(BookingDto bookingDto) {
        Booking booking = bookingMapper.toEntityWithRelation(bookingDto);
        booking.setResultPrice(calculateResultPrice(booking));
        Booking resultBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(resultBooking);
    }

    private BigDecimal calculateResultPrice(Booking booking) {
        long period = ChronoUnit.DAYS.between(booking.getDateStart(), booking.getDateFinish());
        return booking.getAdvert().getPrice().multiply(valueOf(period));
    }
}
