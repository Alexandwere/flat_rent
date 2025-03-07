package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
import com.javaacademy.flat_rent.repository.BookingRepository;
import com.javaacademy.flat_rent.repository.ClientRepository;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
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

import static com.javaacademy.flat_rent.enums.ApartmentType.ONE_ROOM;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Тесты контроллера клиентов")
public class ClientControllerTest {
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String HOUSE = "1";
    private static final String DESCRIPTION = "Описание";
    private static final String NAME = "Petr";
    private static final String EMAIL = "petr@mail.ru";
    private static final LocalDate START_DATE = LocalDate.parse("2025-10-01");
    private static final LocalDate FINISH_DATE = LocalDate.parse("2025-10-10");
    private static final String CLEAN_TABLES = """
            delete from booking;
            delete from advert;
            delete from client;
            delete from apartment;
            """;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/client")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach()
    public void cleanUpDatabase() {
        jdbcTemplate.execute(CLEAN_TABLES);
    }

    @Test
    @DisplayName("Успешное удаление клиента и его бронирований.")
    public void deleteSuccess() {
        Apartment apartment = saveApartment();
        Advert advert = saveAdvert(apartment);
        Client client = saveClient();
        Booking booking = saveBooking(advert, client);

        given(requestSpecification)
                .pathParam("id", client.getId())
                .delete("/{id}")
                .then()
                .spec(responseSpecification)
                .statusCode(OK.value());

        assertFalse(clientRepository.existsById(client.getId()));
        assertFalse(bookingRepository.existsById(booking.getId()));
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

    private Booking saveBooking(Advert advert, Client client) {
        Booking booking = Booking.builder()
                .advert(advert)
                .client(client)
                .dateStart(START_DATE)
                .dateFinish(FINISH_DATE)
                .resultPrice(BigDecimal.TEN)
                .build();
        return bookingRepository.save(booking);
    }
}
