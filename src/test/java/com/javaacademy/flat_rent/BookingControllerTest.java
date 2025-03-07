package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.BookingDto;
import com.javaacademy.flat_rent.dto.BookingDtoRs;
import com.javaacademy.flat_rent.dto.ClientDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.mapper.ClientMapper;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.javaacademy.flat_rent.enums.ApartmentType.ONE_ROOM;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Тесты контроллера бронирований")
public class BookingControllerTest {
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String HOUSE = "1";
    private static final String DESCRIPTION = "Описание";
    private static final String NAME = "Petr";
    private static final String EMAIL = "petr@mail.ru";
    private static final int NON_EXISTENT_ID = -1;
    private static final LocalDate START_DATE = LocalDate.parse("2025-10-01");
    private static final LocalDate FINISH_DATE = LocalDate.parse("2025-10-10");
    private static final int EXPECTED_PAGE_NUMBER = 0;
    private static final int EXPECTED_PAGE_SIZE = 20;
    private static final int EXPECTED_CONTENT_SIZE = 1;
    private static final int EXPECTED_TOTAL_ELEMENTS = 1;
    private static final int EXPECTED_TOTAL_PAGES = 1;
    private static final String CLEAN_TABLES = """
            delete from booking;
            delete from advert;
            delete from client;
            delete from apartment;
            """;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach()
    public void cleanUpDatabase() {
        jdbcTemplate.execute(CLEAN_TABLES);
    }

    @Test
    @DisplayName("Успешное бронирование, при незаполненности id у клиента.")
    public void createWithNewClientSuccess() {
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);

        ClientDto clientDto = ClientDto.builder()
                .name(NAME)
                .email(EMAIL)
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
        assertEquals(START_DATE, bookingDtoRs.getDateStart());
        assertEquals(FINISH_DATE, bookingDtoRs.getDateFinish());
    }

    @Test
    @DisplayName("Успешное бронирование, при указанном id у клиента.")
    public void createWithExistsClientSuccess() {
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);
        Client client = saveClient();

        ClientDto clientDto = ClientDto.builder()
                .id(client.getId())
                .name(NAME)
                .email(EMAIL)
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

        assertNotNull(bookingDtoRs.getId());
        assertTrue(bookingRepository.existsById(bookingDtoRs.getId()));
        assertEquals(START_DATE, bookingDtoRs.getDateStart());
        assertEquals(FINISH_DATE, bookingDtoRs.getDateFinish());
    }

    @Test
    @DisplayName("Неуспешное бронирование при существующем бронировании на эти даты: с 5.10 по 6.10.")
    public void createBookingFailed() {
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);
        Client client = saveClient();
        saveBooking(advert, client);

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
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);
        Client client = saveClient();
        saveBooking(advert, client);

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
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);
        Client client = saveClient();
        saveBooking(advert, client);

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
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);
        Client client = saveClient();
        saveBooking(advert, client);

        Response response = given(requestSpecification)
                .param("pageNumber", EXPECTED_PAGE_NUMBER)
                .param("email", client.getEmail())
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(OK.value())
                .extract()
                .response();

        int pageNumber = response.jsonPath().getInt("pageable.pageNumber");
        int totalPages = response.jsonPath().getInt("totalPages");
        int size = response.jsonPath().getInt("size");
        int totalElements = response.jsonPath().getInt("totalElements");
        List<BookingDtoRs> content = response.jsonPath().getList("content", BookingDtoRs.class);

        assertFalse(content.isEmpty());
        assertEquals(EXPECTED_PAGE_SIZE, size);
        assertEquals(EXPECTED_TOTAL_ELEMENTS, totalElements);
        assertEquals(EXPECTED_PAGE_NUMBER, pageNumber);
        assertEquals(EXPECTED_TOTAL_PAGES, totalPages);
        assertEquals(EXPECTED_CONTENT_SIZE, content.size());
    }

    @Test
    @DisplayName("Бронирование на несуществующие апартаменты - ошибка.")
    public void createWithNoExistsApartmentUnsuccessful() {
        BookingDto bookingDto = BookingDto.builder()
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .clientDto(
                        ClientDto.builder()
                                .name(NAME)
                                .email(EMAIL)
                                .build())
                .advertId(NON_EXISTENT_ID)
                .build();

        given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(NOT_FOUND.value());
    }

    @Test
    @DisplayName("Бронирование на не активное объявление - ошибка.")
    public void createWithNonActiveAdvertUnsuccessful() {
        Apartment apartment = saveApartment();
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description(DESCRIPTION)
                .apartment(apartment)
                .isActive(false)
                .build();
        advertRepository.save(advert);

        BookingDto bookingDto = BookingDto.builder()
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .clientDto(
                        ClientDto.builder()
                                .name(NAME)
                                .email(EMAIL)
                                .build())
                .advertId(advert.getId())
                .build();

        given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CONFLICT.value());
    }

    @Test
    @DisplayName("Сохранение с введенным ID - ошибка.")
    public void createWithIdUnsuccessful() {
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);

        BookingDto bookingDto = BookingDto.builder()
                .id(NON_EXISTENT_ID)
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .clientDto(
                        ClientDto.builder()
                                .name(NAME)
                                .email(EMAIL)
                                .build())
                .advertId(advert.getId())
                .build();

        given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Создание клиента вместе с бронированием - ошибка, клиент существует.")
    public void createWithCreateNewClientUnsuccessful() {
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);
        saveClient();

        BookingDto bookingDto = BookingDto.builder()
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .clientDto(
                        ClientDto.builder()
                                .name(NAME)
                                .email(EMAIL)
                                .build())
                .advertId(advert.getId())
                .build();

        given(requestSpecification)
                .body(bookingDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CONFLICT.value());
    }

    private Apartment saveApartment() {
        Apartment apartment = Apartment.builder()
                .city(CITY)
                .street(STREET)
                .house(HOUSE)
                .apartmentType(ONE_ROOM)
                .build();
        return apartmentRepository.save(apartment);
    }

    private Advert saveAdvert(Apartment apartment) {
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description(DESCRIPTION)
                .apartment(apartment)
                .isActive(true)
                .build();
        return advertRepository.save(advert);
    }

    private Client saveClient() {
        Client client = Client.builder()
                .name(NAME)
                .email(EMAIL)
                .build();
        return clientRepository.save(client);
    }

    private void saveBooking(Advert advert, Client client) {
        Booking booking = Booking.builder()
                .advert(advert)
                .client(client)
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .resultPrice(BigDecimal.TEN)
                .build();
        bookingRepository.save(booking);
    }

}
