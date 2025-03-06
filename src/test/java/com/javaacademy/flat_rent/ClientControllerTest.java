package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.entity.Booking;
import com.javaacademy.flat_rent.entity.Client;
import com.javaacademy.flat_rent.enums.ApartmentType;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Тесты контроллера клиентов")
public class ClientControllerTest {

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/client")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    private static final int NO_EXISTS_CLIENT_ID = -1;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void cleanup() {
        bookingRepository.deleteAll();
        advertRepository.deleteAll();
        clientRepository.deleteAll();
        apartmentRepository.deleteAll();
    }

    @Test
    @DisplayName("Успешное удаление клиента и его бронирований.")
    public void deleteSuccess() {
        Client client = Client.builder()
                .name("Petr")
                .email("petr@mail.com")
                .build();
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

        LocalDate startDate = LocalDate.parse("2025-03-01");
        LocalDate finishDate = LocalDate.parse("2025-03-03");
        Booking booking = Booking.builder()
                .client(client)
                .advert(advert)
                .dateFinish(finishDate)
                .dateStart(startDate)
                .resultPrice(BigDecimal.TEN)
                .build();
        bookingRepository.save(booking);

        given(requestSpecification)
                .pathParams("id", client.getId())
                .delete("/{id}")
                .then()
                .spec(responseSpecification)
                .statusCode(OK.value());

        assertFalse(clientRepository.existsById(client.getId()));
        assertFalse(bookingRepository.existsById(booking.getId()));
    }

    @Test
    @DisplayName("Удалить несуществующего пользователя")
    public void deleteNoExistClient() {
        given(requestSpecification)
                .pathParams("id", NO_EXISTS_CLIENT_ID)
                .delete("/{id}")
                .then()
                .spec(responseSpecification)
                .statusCode(OK.value());
    }
}
