package com.javaacademy.flat_rent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        Contact contact = new Contact()
                .email("lazukov_ao@mail.ru")
                .name("Alexander");

        Info info = new Info()
                .title("API сервиса посуточного бронирования апартаментов")
                .contact(contact)
                .description("Сервис позволяет сохранять квартиры, объявления, клиентов, "
                        + "управлять бронированиями.");

        return new OpenAPI()
                .info(info);
    }
}
