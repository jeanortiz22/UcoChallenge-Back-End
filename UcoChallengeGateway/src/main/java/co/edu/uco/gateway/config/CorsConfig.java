package co.edu.uco.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de CORS para el API Gateway.
 * Permite peticiones desde el frontend en localhost:5173.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Origen del frontend
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
        
        // Permitir credenciales (cookies, authorization headers)
        corsConfig.setAllowCredentials(true);
        
        // Métodos HTTP permitidos
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Permitir todos los headers
        corsConfig.setAllowedHeaders(List.of("*"));
        
        // Headers que el frontend puede leer
        corsConfig.setExposedHeaders(Arrays.asList(
            "Authorization", "Content-Type"
        ));
        
        // Tiempo de caché para preflight requests
        corsConfig.setMaxAge(3600L);
        
        // Aplicar la configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}