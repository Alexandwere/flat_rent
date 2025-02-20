package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.enums.CountRoomsType;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class FlatRentApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FlatRentApplication.class, args);

        ApartmentRepository apartmentRepository = context.getBean(ApartmentRepository.class);
        AdvertRepository advertRepository = context.getBean(AdvertRepository.class);
        BookingRepository bookingRepository = context.getBean(BookingRepository.class);
        ClientRepository clientRepository = context.getBean(ClientRepository.class);

        apartmentRepository.save(Apartment.builder()
                .city("Perm")
                .street("Testovaya")
                .houseNumber("43")
                .countRoomsType(CountRoomsType.STUDIO)
                .build());

        clientRepository.save(Client.builder()
                .email("emil@email.com")
                .name("Катя")
                .build());

        advertRepository.save(Advert.builder()
                .apartmentId(1)
                .price(BigDecimal.TEN)
                .description("Первое объявление")
                .isActive(true)
                .build());

        bookingRepository.save(Booking.builder()
                .advertId(1)
                .clientId(1)
                .startDate(LocalDateTime.parse("2025-02-20T14:00"))
                .endDate(LocalDateTime.parse("2025-02-21T12:00"))
                .price(BigDecimal.TEN)
                .build());
    }

}
