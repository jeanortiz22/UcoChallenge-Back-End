package co.edu.uco.parametersservice.infrastructure;

import co.edu.uco.parametersservice.infrastructure.security.GatewayOnlyFilter; // ✅ local
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Value("${gateway.security.header:}") String headerName,
            @Value("${gateway.security.secret:}") String secret
    ) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/parameters/api/v1/parameters/**").permitAll() // quítalo si quieres proteger
                .anyRequest().permitAll()
            );

        // Inserta el filtro antes de UsernamePasswordAuthenticationFilter
        http.addFilterBefore(new GatewayOnlyFilter(headerName, secret), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
