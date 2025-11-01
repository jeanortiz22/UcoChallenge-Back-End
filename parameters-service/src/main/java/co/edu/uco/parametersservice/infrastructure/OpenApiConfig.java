package co.edu.uco.parametersservice.infrastructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Parameters Service API",
                version = "1.0",
                description = "Servicio centralizado para exponer parámetros de configuración funcional del reto UCO.",
                contact = @Contact(name = "Equipo UCO Challenge", email = "soporte@uco.edu.co"),
                license = @License(name = "MIT")
        )
)
public class OpenApiConfig {
}