package co.edu.uco.messageservice.infrastructure;

import co.edu.uco.messageservice.infrastructure.security.GatewayOnlyFilter; // 👈 local
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Value("${gateway.security.header:}") String headerName,
            @Value("${gateway.security.secret:}") String secret
    ) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.cors(cors -> cors.configurationSource(req -> {
            var cfg = new CorsConfiguration();
            cfg.setAllowedOrigins(List.of("http://localhost:5173"));
            cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
            cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","Accept-Language", headerName));
            cfg.setAllowCredentials(true);
            return cfg;
        }));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/messages/api/v1/messages/**").permitAll()
                .anyRequest().permitAll()
        );

        http.addFilterBefore(new GatewayOnlyFilter(headerName, secret), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
