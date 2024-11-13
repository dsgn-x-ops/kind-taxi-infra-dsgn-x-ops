package com.taxidata.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taxiDataOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taxi Data API")
                        .description("API for accessing taxi ride data")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@taxidata.com")));
    }
}