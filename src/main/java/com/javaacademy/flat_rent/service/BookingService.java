package com.javaacademy.flat_rent.service;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.exception.ClientAlreadyExistsException;
import com.javaacademy.flat_rent.exception.EntityNotFoundException;
import com.javaacademy.flat_rent.exception.FilledIdException;
import com.javaacademy.flat_rent.exception.IntersectionDateException;
import com.javaacademy.flat_rent.exception.NotActiveAdvertException;
import com.javaacademy.flat_rent.mapper.BookingMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
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
import java.util.Objects;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private static final int PAGE_SIZE = 20;

    private final BookingRepository bookingRepository;
    private final AdvertRepository advertRepository;
    private final BookingMapper bookingMapper;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @Transactional
    public BookingDtoRs save(BookingDto bookingDto) {
        if (bookingDto.getId() != null) {
            throw new FilledIdException("ID бронирования должен быть null");
        }
        ClientDto clientDto = bookingDto.getClientDto();
        if (clientDto.getId() == null) {
            checkClient(clientDto);
            clientDto = clientService.save(clientDto);
            bookingDto.setClientDto(clientDto);
        }
        if (!clientRepository.existsById(clientDto.getId())) {
            throw new EntityNotFoundException("Клиента с ID = %s не существует."
                    .formatted(clientDto.getId()));
        }
        checkAdvert(bookingDto);
        checkDates(bookingDto);
        Booking booking = bookingMapper.toEntityWithRelation(bookingDto);
        booking.setResultPrice(calculateResultPrice(booking));
        booking = bookingRepository.save(booking);
        log.trace("Бронирование сохранено.");
        return bookingMapper.toDto(booking);
    }

    @Transactional(readOnly = true)
    public Page<BookingDtoRs> findAllByEmail(Integer pageNumber, String email) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Booking> bookings = bookingRepository.findAllByClientEmail(email, pageRequest);
        log.trace("Выполнен поиск бронирований по email.");
        return bookings.map(bookingMapper::toDto);
    }

    private BigDecimal calculateResultPrice(Booking booking) {
        long period = ChronoUnit.DAYS.between(booking.getDateStart(), booking.getDateFinish());
        return booking.getAdvert().getPrice().multiply(valueOf(period));
    }

    private void checkDates(BookingDto bookingDto) {
        if (bookingDto.getDateStart().isAfter(bookingDto.getDateFinish())) {
            throw new IntersectionDateException("Дата начала должна быть раньше даты окончания");
        }
        if (!bookingRepository.canBook(
                bookingDto.getAdvertId(),
                bookingDto.getDateStart(),
                bookingDto.getDateFinish())) {
            throw new IntersectionDateException("Невозможно забронировать в эти даты.");
        }
        log.trace("Выполнена проверка дат.");
    }

    private void checkAdvert(BookingDto bookingDto) {
        int id = bookingDto.getAdvertId();
        Advert advert = advertRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Объявление с ID = %s не найдено."
                        .formatted(id)));
        if (!advert.getIsActive()) {
            throw new NotActiveAdvertException("Объявление с ID = %s не активно"
                    .formatted(id));
        }
    }

    private void checkClient(ClientDto clientDto) {
        Optional<Client> optional = clientRepository.findByEmail(clientDto.getEmail());
        if (optional.isPresent()) {
            Client client = optional.get();
            if (Objects.equals(client.getEmail(), clientDto.getEmail())) {
                throw new ClientAlreadyExistsException("Клиент с такой почтой уже существует.");
            }
        }
    }
}
