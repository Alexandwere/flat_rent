package com.javaacademy.flat_rent;

import com.javaacademy.flat_rent.dto.AdvertDto;
import com.javaacademy.flat_rent.dto.AdvertDtoRs;
import com.javaacademy.flat_rent.dto.ApartmentDto;
import com.javaacademy.flat_rent.entity.Advert;
import com.javaacademy.flat_rent.entity.Apartment;
import com.javaacademy.flat_rent.repository.AdvertRepository;
import com.javaacademy.flat_rent.repository.ApartmentRepository;
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
import java.util.List;

import static com.javaacademy.flat_rent.enums.ApartmentType.ONE_ROOM;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("Тесты контроллера объявлений")
public class AdvertControllerTest {
    private static final int NON_EXISTENT_ID = -1;
    private static final int COUNT_ADVERTS_BY_CITY = 2;
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String HOUSE = "1";
    private static final String DESCRIPTION = "Описание";
    private static final BigDecimal EXPECTED_PRICE = BigDecimal.ONE;
    private static final int EXPECTED_PAGE_SIZE = 10;
    private static final int EXPECTED_TOTAL_ADVERT = 2;
    private static final int EXPECTED_PAGE_NUMBER = 0;
    private static final int EXPECTED_TOTAL_PAGES = 1;
    private static final String CLEAN_TABLES = """
            delete from booking;
            delete from advert;
            delete from client;
            delete from apartment;
            """;

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
    private JdbcTemplate jdbcTemplate;

    @BeforeEach()
    public void cleanUpDatabase() {
        jdbcTemplate.execute(CLEAN_TABLES);
    }

    @Test
    @DisplayName("Успешное создание объявления")
    public void createAdvertSuccess() {
        Apartment apartment = saveApartment();
        AdvertDto advertDtoRq = AdvertDto.builder()
                .apartmentId(apartment.getId())
                .price(BigDecimal.TEN)
                .description(DESCRIPTION)
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
                .apartmentId(NON_EXISTENT_ID)
                .price(BigDecimal.TEN)
                .description(DESCRIPTION)
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
        Apartment apartment = saveApartment();
        saveAdvert(apartment);
        saveAdvert(apartment);

        Apartment apartment2 = Apartment.builder()
                .city("OtherCity")
                .street(STREET)
                .house(HOUSE)
                .apartmentType(ONE_ROOM)
                .build();
        apartment2 = apartmentRepository.save(apartment2);
        saveAdvert(apartment2);

        Response response = given(requestSpecification)
                .queryParam("city", apartment.getCity())
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
        List<AdvertDtoRs> content = response.jsonPath().getList("content", AdvertDtoRs.class);

        AdvertDtoRs firstAdvertDto = content.stream().findFirst().orElseThrow();
        BigDecimal resultPrice = firstAdvertDto.getPrice();
        assertEquals(COUNT_ADVERTS_BY_CITY, content.size());
        assertEquals(0, EXPECTED_PRICE.compareTo(resultPrice));
        assertEquals(EXPECTED_PAGE_SIZE, size);
        assertEquals(EXPECTED_TOTAL_ADVERT, totalElements);
        assertEquals(EXPECTED_PAGE_NUMBER, pageNumber);
        assertEquals(EXPECTED_TOTAL_PAGES, totalPages);

        Apartment resultApartment = apartmentRepository.findById(firstAdvertDto.getApartment().getId()).orElseThrow();
        String resultCity = resultApartment.getCity();
        assertEquals(CITY, resultCity);
    }

    @Test
    @DisplayName("Создание объявления с введенным id - ошибка")
    public void createWithIdFailed() {
        Apartment apartment = saveApartment();
        AdvertDto advertDtoRq = AdvertDto.builder()
                .id(NON_EXISTENT_ID)
                .apartmentId(apartment.getId())
                .price(BigDecimal.TEN)
                .description(DESCRIPTION)
                .isActive(true)
                .build();

        given(requestSpecification)
                .body(advertDtoRq)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(BAD_REQUEST.value());
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

    private void saveAdvert(Apartment apartment) {
        Advert advert = Advert.builder()
                .price(BigDecimal.ONE)
                .description(DESCRIPTION)
                .apartment(apartment)
                .isActive(true)
                .build();
        advertRepository.save(advert);
    }

}
