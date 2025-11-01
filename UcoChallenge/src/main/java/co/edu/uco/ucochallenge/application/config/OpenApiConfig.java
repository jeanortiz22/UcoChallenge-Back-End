package co.edu.uco.ucochallenge.application.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "UCO Challenge Core API",
                version = "1.0",
                description = "Servicios principales del ecosistema UCO Challenge (usuarios, catálogos y utilidades)",
                contact = @Contact(name = "Equipo UCO Challenge", email = "soporte@uco.edu.co"),
                license = @License(name = "MIT")
        ),
        security = {@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)}
)
@SecurityScheme(
        name = OpenApiConfig.BEARER_SCHEME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Incluya un token de acceso emitido por Auth0 en el encabezado Authorization (Bearer)."
)
public class OpenApiConfig {

    static final String BEARER_SCHEME = "bearerAuth";
}