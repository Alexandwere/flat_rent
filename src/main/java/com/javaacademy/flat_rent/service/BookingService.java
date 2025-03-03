package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.exception.EntityNotFoundException;
import com.javaacademy.flat_rent.exception.IntersectionDateException;
import com.javaacademy.flat_rent.mapper.BookingMapper;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.math.BigDecimal.valueOf;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @Transactional
    public BookingDtoRs save(BookingDto bookingDto) {
        ClientDto clientDto = bookingDto.getClientDto();
        if (!clientRepository.existsById(clientDto.getId())) {
            throw new EntityNotFoundException("Клиента с ID = %s не существует.".formatted(clientDto.getId()));
        }

        if (bookingDto.getDateStart().isAfter(bookingDto.getDateFinish())) {
            throw new IllegalArgumentException("Дата начала должна быть раньше даты окончания");
        }

        List<Booking> bookingsByAdvert = bookingRepository.findAllByAdvertIdUsingNativeSQL(bookingDto.getAdvertId());
        if (!canBook(bookingsByAdvert, bookingDto)) {
            throw new IntersectionDateException("Невозможно забронировать в эти даты.");
        }

        if (clientDto.getId() == null) {
            clientService.save(clientDto);
        }

        Booking booking = bookingMapper.toEntityWithRelation(bookingDto);
        booking.setResultPrice(calculateResultPrice(booking));
        Booking resultBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(resultBooking);
    }

    private boolean canBook(List<Booking> existingBookings, BookingDto bookingDto) {
        for (Booking booking : existingBookings) {
            if (!(booking.getDateFinish().isBefore(bookingDto.getDateStart())
                    || booking.getDateStart().isAfter(bookingDto.getDateFinish()))) {
                return false;
            }
        }
        return true;
    }

    private BigDecimal calculateResultPrice(Booking booking) {
        long period = ChronoUnit.DAYS.between(booking.getDateStart(), booking.getDateFinish());
        return booking.getAdvert().getPrice().multiply(valueOf(period));
    }
}
