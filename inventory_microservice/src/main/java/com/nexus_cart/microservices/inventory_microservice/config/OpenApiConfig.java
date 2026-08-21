package com.nexus_cart.microservices.inventory_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventoryServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NexusCart - Inventory Microservice API")
                        .description("RESTful API documentation for stock creation, querying, and deduction.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nexus Cart Engineering Team")
                                .email("dev@nexuscart.com")));
    }
}