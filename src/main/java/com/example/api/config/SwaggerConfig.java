package com.example.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Spring Boot Rest Api",
                description = "Spring Boot Rest Api Specification",
                version = "v1"))
@Configuration
public class SwaggerConfig {
}
