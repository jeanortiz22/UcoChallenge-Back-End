// messages-service (8083)
// co.edu.uco.messages.infrastructure.security.SecurityConfig
package co.edu.uco.messageservice.infrastructure;

import co.edu.uco.ucochallenge.infrastructure.security.GatewayOnlyFilter; // o copia el filtro aquí y ajusta el import
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            org.springframework.security.config.annotation.web.builders.HttpSecurity http,
            @Value("${gateway.security.header}") String headerName,
            @Value("${gateway.security.secret}") String secret
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                		.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/messages/api/v1/messages/**").permitAll() // público
                        .anyRequest().permitAll() // (o .authenticated() si luego añades auth a otros paths)
                );
        // ⛔ No configures .oauth2ResourceServer(oauth -> oauth.jwt())

        // Si quieres que SOLO el gateway consuma el catálogo, deja el filtro:
        http.addFilterBefore(new GatewayOnlyFilter(headerName, secret), BasicAuthenticationFilter.class);

        return http.build();
    }
}
