package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.enums.ApartmentType;
import com.javaacademy.flat_rent.mapper.ClientMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookingControllerTest {
    private static final LocalDate START_DATE = LocalDate.parse("2025-10-01");
    private static final LocalDate FINISH_DATE = LocalDate.parse("2025-10-10");
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 20;
    private static final int CONTENT_SIZE = 1;
    private static final int TOTAL_PAGES = 1;
    private static final int TOTAL_ELEMENTS = 1;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/booking")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @BeforeEach
    public void cleanup() {
        bookingRepository.deleteAll();
        advertRepository.deleteAll();
        clientRepository.deleteAll();
        apartmentRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное бронирование, при незаполненности id у клиента.")
    public void createWithNewClientSuccess() {
        Apartment apartment = Apartment.builder()
                .city("city")
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description("Описание")
                .apartment(apartment)
                .isActive(true)
                .build();
        advertRepository.save(advert);

        ClientDto clientDto = ClientDto.builder()
                .name("Petr")
                .email("petr@mail.com")
                .build();

        BookingDto bookingDto = BookingDto.builder()
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .clientDto(clientDto)
                .advertId(advert.getId())
                .build();

        BookingDtoRs bookingDtoRs = given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CREATED.value())
                .extract()
                .body()
                .as(BookingDtoRs.class);

        ClientDto clientDtoRs = bookingDtoRs.getClient();

        assertNotNull(bookingDtoRs.getId());
        assertNotNull(clientDtoRs.getId());
        assertTrue(clientRepository.existsById(clientDtoRs.getId()));
        assertTrue(bookingRepository.existsById(bookingDtoRs.getId()));
        assertEquals(bookingDto.getDateStart(), bookingDtoRs.getDateStart());
        assertEquals(bookingDto.getDateFinish(), bookingDtoRs.getDateFinish());
    }

    @Test
    @DisplayName("Успешное бронирование, при указанном id у клиента.")
    public void createWithExistsClientSuccess() {
        Apartment apartment = Apartment.builder()
                .city("city")
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description("Описание")
                .apartment(apartment)
                .isActive(true)
                .build();
        advertRepository.save(advert);

        Client client = Client.builder()
                .name("Petr")
                .email("petr@mail.com")
                .build();
        ClientDto clientDto = clientMapper.toDto(client);

        BookingDto bookingDto = BookingDto.builder()
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .clientDto(clientDto)
                .advertId(advert.getId())
                .build();

        BookingDtoRs bookingDtoRs = given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CREATED.value())
                .extract()
                .body()
                .as(BookingDtoRs.class);

        assertNotNull(bookingDtoRs.getId());
        assertTrue(bookingRepository.existsById(bookingDtoRs.getId()));
        assertEquals(bookingDto.getDateStart(), bookingDtoRs.getDateStart());
        assertEquals(bookingDto.getDateFinish(), bookingDtoRs.getDateFinish());
    }

    @Test
    @DisplayName("Неуспешное бронирование при существующем бронировании на эти даты: с 5.10 по 6.10.")
    public void createBookingFailed() {
        Apartment apartment = Apartment.builder()
                .city("city")
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description("Описание")
                .apartment(apartment)
                .isActive(true)
                .build();
        advertRepository.save(advert);
        Client client = Client.builder()
                .name("Petr")
                .email("petr@mail.com")
                .build();
        Booking booking = Booking.builder()
                .advert(advert)
                .client(client)
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .resultPrice(BigDecimal.TEN)
                .build();
        bookingRepository.save(booking);

        BookingDto bookingDto = BookingDto.builder()
                .advertId(advert.getId())
                .dateStart(LocalDate.parse("2025-10-05"))
                .dateFinish(LocalDate.parse("2025-10-06"))
                .clientDto(clientMapper.toDto(client))
                .build();
        given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CONFLICT.value());
    }

    @Test
    @DisplayName("Неуспешное бронирование при существующем бронировании на эти даты: с 29.09 по 2.10.")
    public void createBookingUnsuccessful2() {
        Apartment apartment = Apartment.builder()
                .city("city")
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description("Описание")
                .apartment(apartment)
                .isActive(true)
                .build();
        advertRepository.save(advert);
        Client client = Client.builder()
                .name("Petr")
                .email("petr@mail.com")
                .build();
        Booking booking = Booking.builder()
                .advert(advert)
                .client(client)
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .resultPrice(BigDecimal.TEN)
                .build();
        bookingRepository.save(booking);

        BookingDto bookingDto = BookingDto.builder()
                .advertId(advert.getId())
                .dateStart(LocalDate.parse("2025-09-29"))
                .dateFinish(LocalDate.parse("2025-10-02"))
                .clientDto(clientMapper.toDto(client))
                .build();
        given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CONFLICT.value());
    }

    @Test
    @DisplayName("Неуспешное бронирование при существующем бронировании на эти даты: с 9.10 по 10.10.")
    public void createBookingUnsuccessful3() {
        Apartment apartment = Apartment.builder()
                .city("city")
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description("Описание")
                .apartment(apartment)
                .isActive(true)
                .build();
        advertRepository.save(advert);
        Client client = Client.builder()
                .name("Petr")
                .email("petr@mail.com")
                .build();
        Booking booking = Booking.builder()
                .advert(advert)
                .client(client)
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .resultPrice(BigDecimal.TEN)
                .build();
        bookingRepository.save(booking);

        BookingDto bookingDto = BookingDto.builder()
                .advertId(advert.getId())
                .dateStart(LocalDate.parse("2025-10-09"))
                .dateFinish(LocalDate.parse("2025-10-10"))
                .clientDto(clientMapper.toDto(client))
                .build();
        given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CONFLICT.value());
    }

    @Test
    @DisplayName("Успешный поиск всех бронирований по email клиента.")
    public void getByClientEmailSuccess() {
        Apartment apartment = Apartment.builder()
                .city("city")
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description("Описание")
                .apartment(apartment)
                .isActive(true)
                .build();
        advertRepository.save(advert);
        Client client = Client.builder()
                .name("Petr")
                .email("petr@mail.com")
                .build();
        Booking booking = Booking.builder()
                .advert(advert)
                .client(client)
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .resultPrice(BigDecimal.TEN)
                .build();
        bookingRepository.save(booking);

        Page<BookingDtoRs> responseBody = given(requestSpecification)
                .param("pageNumber", PAGE_NUMBER)
                .param("email", client.getEmail())
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() {
                });

        assertFalse(responseBody.getContent().isEmpty());
        assertEquals(CONTENT_SIZE, responseBody.getContent().size());
        assertEquals(PAGE_SIZE, responseBody.getPageable().getPageSize());
        assertEquals(TOTAL_ELEMENTS, responseBody.getTotalElements());
        assertEquals(TOTAL_PAGES, responseBody.getTotalPages());
    }

}
