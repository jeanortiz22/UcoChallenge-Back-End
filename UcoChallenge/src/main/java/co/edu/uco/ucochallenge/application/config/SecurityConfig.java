package co.edu.uco.ucochallenge.application.config;

import co.edu.uco.ucochallenge.infrastructure.security.GatewayOnlyFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
    		final GatewayOnlyFilter gatewayOnlyFilter,
            final JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        // 🔥 BLOQUEA acceso directo al backend y solo permite API desde el Gateway
        http.addFilterBefore(gatewayOnlyFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(
        @Value("${security.jwt.authority-claims:permissions,roles}") final String authorityClaims,
        @Value("${security.jwt.authority-prefix:}") final String authorityPrefix) {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter(authorityClaims, authorityPrefix));
        return converter;
    }

    private Converter<Jwt, Collection<GrantedAuthority>> grantedAuthoritiesConverter(
        final String authorityClaims,
        final String authorityPrefix) {
        return jwt -> {
            final Set<String> authorities = new LinkedHashSet<>();
            final List<String> claimNames = parseClaimNames(authorityClaims);
            claimNames.forEach(claim -> addAuthoritiesFromClaim(jwt, authorities, claim));

            return authorities.stream()
                .map(authority -> authorityPrefix + authority)
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
        };
    }

    private List<String> parseClaimNames(final String authorityClaims) {
        final List<String> claims = Arrays.stream(authorityClaims.split(","))
            .map(String::trim)
            .filter(claim -> !claim.isBlank())
            .toList();
        return claims.isEmpty() ? List.of("permissions") : claims;
    }

    private void addAuthoritiesFromClaim(final Jwt jwt,
                                         final Collection<String> authorities,
                                         final String claimName) {
        final Object claim = jwt.getClaims().get(claimName);
        if (claim instanceof Collection<?> collection) {
            collection.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::trim)
                .filter(authority -> !authority.isBlank())
                .forEach(authorities::add);
        } else if (claim instanceof String value) {
            final String trimmed = value.trim();
            if (!trimmed.isBlank()) {
                authorities.add(trimmed);
            }
        }
    }

    @Bean
    public GatewayOnlyFilter gatewayOnlyFilter(
        @Value("${gateway.security.header:X-Gateway-Request}") final String gatewayHeader,
        @Value("${gateway.security.secret:}") final String gatewaySecret) {
        return new GatewayOnlyFilter(gatewayHeader, gatewaySecret);
    }

    @Bean
    public JwtDecoder jwtDecoder(
        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") final String issuer,
        @Value("${spring.security.oauth2.resourceserver.jwt.audience}") final String audience) {

        final NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuer);
        final OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        final OAuth2TokenValidator<Jwt> audienceValidator = new JwtClaimValidator<>("aud", aud -> aud instanceof Iterable<?> iterable && containsAudience(iterable, audience));
        final OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    private boolean containsAudience(final Iterable<?> audiences, final String audience) {
        for (final Object value : audiences) {
            if (audience.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
