package com.bankai.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .description("This is a REST API for User Service operations")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Bankai Team")
                                .email("support@bankai.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
        }
} 