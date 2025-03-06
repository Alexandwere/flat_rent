package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.enums.ApartmentType;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Тесты контроллера объявлений")
public class AdvertControllerTest {
    private static final int NO_EXISTS_APARTMENT_ID = -1;
    private static final int COUNT_ADVERTS = 2;
    private static final String CITY = "city";
    private static final BigDecimal EXPECTED_PRICE = BigDecimal.ONE;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/advert")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

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
    @DisplayName("Успешное создание объявления")
    public void createAdvertSuccess() {
        Apartment apartment = Apartment.builder()
                .city(CITY)
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        apartment = apartmentRepository.save(apartment);

        AdvertDto advertDtoRq = AdvertDto.builder()
                .apartmentId(apartment.getId())
                .price(BigDecimal.TEN)
                .description("Однокомнатная")
                .isActive(true)
                .build();

        AdvertDtoRs advertDtoRs = given(requestSpecification)
                .body(advertDtoRq)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CREATED.value())
                .extract()
                .body()
                .as(AdvertDtoRs.class);

        assertNotNull(advertDtoRs.getId());
        assertEquals(advertDtoRq.getPrice(), advertDtoRs.getPrice());
        assertEquals(advertDtoRq.getIsActive(), advertDtoRs.getIsActive());
        assertEquals(advertDtoRq.getDescription(), advertDtoRs.getDescription());

        ApartmentDto apartmentDto = advertDtoRs.getApartment();
        assertEquals(apartment.getId(), apartmentDto.getId());
    }

    @Test
    @DisplayName("Создание объявления на не существующую квартиру - ошибка")
    public void createFailed() {
        AdvertDto advertDtoRq = AdvertDto.builder()
                .apartmentId(NO_EXISTS_APARTMENT_ID)
                .price(BigDecimal.TEN)
                .description("Однокомнатная")
                .isActive(true)
                .build();

        given(requestSpecification)
                .body(advertDtoRq)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(NOT_FOUND.value());
    }

    @Test
    @DisplayName("Успешный поиск объявлений по городу.")
    public void findByCitySuccess() {
        Apartment apartment = Apartment.builder()
                .city(CITY)
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

        Apartment apartment2 = Apartment.builder()
                .city(CITY)
                .street("street")
                .house("2")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert2 = Advert.builder()
                .price(BigDecimal.TEN)
                .description("Описание")
                .apartment(apartment2)
                .isActive(true)
                .build();
        advertRepository.save(advert2);

        Apartment apartment3 = Apartment.builder()
                .city("city3")
                .street("street")
                .house("1")
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();
        Advert advert3 = Advert.builder()
                .price(BigDecimal.ONE)
                .description("Описание")
                .apartment(apartment3)
                .isActive(true)
                .build();
        advertRepository.save(advert3);

        Page<AdvertDto> responseBody = given(requestSpecification)
                .queryParam("city", apartment2.getCity())
                .get()
                .then()
                .spec(responseSpecification)
                .statusCode(OK.value())
                .extract()
                .body()
                .as(new TypeRef<>() {
                });

        AdvertDto firstAdvertDto = responseBody.getContent().stream().findFirst().orElseThrow();
        BigDecimal resultPrice = firstAdvertDto.getPrice();
        assertEquals(COUNT_ADVERTS, responseBody.getContent().size());
        assertEquals(EXPECTED_PRICE, resultPrice);

        Apartment resultApartment = apartmentRepository.findById(firstAdvertDto.getApartmentId()).orElseThrow();
        String resultCity = resultApartment.getCity();
        assertEquals(CITY, resultCity);
    }
}
