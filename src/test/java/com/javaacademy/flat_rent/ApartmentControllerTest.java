package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.enums.ApartmentType;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Тесты контроллера апартаментов")
public class ApartmentControllerTest {
    private static final int ID = 1;
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String HOUSE = "1";
    private static final String CLEAN_TABLES = """
            delete from booking;
            delete from advert;
            delete from client;
            delete from apartment;
            """;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/apartment")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach()
    public void cleanUpDatabase() {
        jdbcTemplate.execute(CLEAN_TABLES);
    }

    @Test
    @DisplayName("Успешное создание апартаментов")
    public void createApartmentSuccess() {
        ApartmentDto apartmentDtoRq = ApartmentDto.builder()
                .city(CITY)
                .street(STREET)
                .house(HOUSE)
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();

        ApartmentDto apartmentDtoRs = given(requestSpecification)
                .body(apartmentDtoRq)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(CREATED.value())
                .extract()
                .body()
                .as(ApartmentDto.class);

        Apartment apartment = apartmentRepository.findById(apartmentDtoRs.getId()).orElseThrow();

        assertNotNull(apartmentDtoRs.getId());
        assertEquals(CITY, apartment.getCity());
        assertEquals(STREET, apartment.getStreet());
        assertEquals(HOUSE, apartment.getHouse());
        assertEquals(apartmentDtoRq.getApartmentType(), apartment.getApartmentType());
    }

    @Test
    @DisplayName("Сохранение апартаментов - ошибка, введен ID")
    public void createApartmentFailedById() {
        ApartmentDto apartmentDtoRq = ApartmentDto.builder()
                .id(ID)
                .city(CITY)
                .street(STREET)
                .house(HOUSE)
                .apartmentType(ApartmentType.ONE_ROOM)
                .build();

        given(requestSpecification)
                .body(apartmentDtoRq)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(BAD_REQUEST.value());
    }
}
