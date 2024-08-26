package dev.awn.customermanagementservice.common.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile("dev")
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Customer Management Service API",
                description = "the Customer Management Service API allows for restricted operations onto customers",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}