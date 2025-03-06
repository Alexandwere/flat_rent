package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.entity.Apartment;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Тесты контроллера апартаментов")
public class ApartmentControllerTest {

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
    @DisplayName("Успешное создание апартаментов")
    public void createApartmentSuccess() {
        ApartmentDto apartmentDtoRq = ApartmentDto.builder()
                .id(null)
                .city("city")
                .street("street")
                .house("1")
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
        assertEquals(apartmentDtoRq.getCity(), apartment.getCity());
        assertEquals(apartmentDtoRq.getHouse(), apartment.getHouse());
        assertEquals(apartmentDtoRq.getApartmentType(), apartment.getApartmentType());
    }
}
