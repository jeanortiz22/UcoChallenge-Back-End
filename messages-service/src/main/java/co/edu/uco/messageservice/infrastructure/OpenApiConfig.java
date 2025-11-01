package co.edu.uco.messageservice.infrastructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Messages Service API",
                version = "1.0",
                description = "Catálogo de mensajes disponibles para las diferentes aplicaciones del reto UCO.",
                contact = @Contact(name = "Equipo UCO Challenge", email = "soporte@uco.edu.co"),
                license = @License(name = "MIT")
        )
)
public class OpenApiConfig {
}
