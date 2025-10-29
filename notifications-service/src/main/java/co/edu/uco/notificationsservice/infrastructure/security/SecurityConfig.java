package co.edu.uco.notificationsservice.infrastructure.security;

import co.edu.uco.ucochallenge.infrastructure.security.GatewayOnlyFilter;
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
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/notifications/api/v1/templates/**").permitAll()
                        .anyRequest().permitAll()
                );

        http.addFilterBefore(new GatewayOnlyFilter(headerName, secret), BasicAuthenticationFilter.class);

        return http.build();
    }
}
