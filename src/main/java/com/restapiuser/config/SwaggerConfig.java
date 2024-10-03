package com.restapiuser.config;

import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("USERS API")
                        .version("1.0")
                        .description("API for Users Management")
                        .contact(new Contact()
                                .name("Jose Ignacio Naranjo Guerra")
                                .email("joseignacio.naranjoguerra@gmail.com")
                                .url("https://www.linkedin.com/in/ignacio-naranjo-guerra-40706a144/")));
    }
}

