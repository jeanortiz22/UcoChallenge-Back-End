package co.edu.uco.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                // Todas las rutas /api/** requieren autenticación
                .pathMatchers("/api/**").authenticated()
                .anyExchange().permitAll()
            )
            // Valida JWT con Auth0
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
            
        return http.build();
    }
}