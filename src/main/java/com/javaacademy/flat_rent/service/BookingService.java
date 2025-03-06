package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.exception.EntityNotFoundException;
import com.javaacademy.flat_rent.exception.IntersectionDateException;
import com.javaacademy.flat_rent.exception.NotActiveAdvertException;
import com.javaacademy.flat_rent.mapper.BookingMapper;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.math.BigDecimal.valueOf;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private static final int PAGE_SIZE = 20;

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @Transactional
    public BookingDtoRs save(BookingDto bookingDto) {
        ClientDto clientDto = bookingDto.getClientDto();
        if (clientDto.getId() == null) {
            clientDto = clientService.save(clientDto);
            bookingDto.setClientDto(clientDto);
        }
        Booking booking = bookingMapper.toEntityWithRelation(bookingDto);
        if (!clientRepository.existsById(booking.getClient().getId())) {
            throw new EntityNotFoundException("Клиента с ID = %s не существует."
                    .formatted(clientDto.getId()));
        }

        checkDates(bookingDto);

        if (!booking.getAdvert().getIsActive()) {
            throw new NotActiveAdvertException("Объявление с ID = %s не активно"
                    .formatted(booking.getAdvert().getId()));
        }
        booking.setResultPrice(calculateResultPrice(booking));
        bookingRepository.save(booking);
        log.info("Бронирование сохранено.");
        return bookingMapper.toDto(booking);
    }

    @Transactional(readOnly = true)
    public Page<BookingDtoRs> findAllByEmail(Integer pageNumber, String email) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Booking> bookings = bookingRepository.findAllByEmail(email, pageRequest);
        log.info("Выполнен поиск бронирований по email.");
        return bookings.map(bookingMapper::toDto);
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

    private void checkDates(BookingDto bookingDto) {
        if (bookingDto.getDateStart().isAfter(bookingDto.getDateFinish())) {
            throw new IntersectionDateException("Дата начала должна быть раньше даты окончания");
        }

        List<Booking> bookingsByAdvert = bookingRepository.findAllByAdvertId(bookingDto.getAdvertId());
        if (!canBook(bookingsByAdvert, bookingDto)) {
            throw new IntersectionDateException("Невозможно забронировать в эти даты.");
        }
        log.info("Выполнена проверка дат.");
    }
}
