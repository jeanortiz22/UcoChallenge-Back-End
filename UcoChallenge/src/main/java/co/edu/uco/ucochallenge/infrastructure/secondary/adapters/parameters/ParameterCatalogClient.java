package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.parameters;

import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Locale;

/**
 * Cliente del Catálogo de Parámetros.
 * - Usa PATH variable que acepta puntos (el service debe tener @GetMapping("/{key:.+}")).
 * - Cachea por 'code' (las claves son case-sensitive).
 * - Si el catálogo responde 404 => retorna null (deja que el caller decida 404).
 */
@Component
public class ParameterCatalogClient implements ParameterCatalogPort {

    private static final Logger log = LoggerFactory.getLogger(ParameterCatalogClient.class);

    // Debe apuntar al parameters-service (p.ej. http://localhost:8082/parameters/api/v1/parameters)
    private final WebClient parametersWebClient;

    public ParameterCatalogClient(@Qualifier("parametersWebClient") WebClient parametersWebClient) {
        this.parametersWebClient = parametersWebClient;
    }

    /** No forzamos lower-case: las keys del catálogo son case-sensitive. */
    private String normalize(String code) {
        return code == null ? "" : code.trim();
    }

    @Override
    @Cacheable(
            cacheNames = "parameters",
            key = "T(java.util.Objects).toString(#code,'')",
            unless = "#result == null || #result.isBlank()"
    )
    public String get(String code, Object... args) {
        if (code == null || code.isBlank()) {
            return null;
        }

        final String key = normalize(code);
        log.debug("Consultando parámetro → code={}, args={}", key, Arrays.toString(args));

        ParameterDto dto = parametersWebClient.get()
                // ✅ PATH variable (el controller del catálogo debe declarar {key:.+})
                .uri(b -> b.path("/parameters/api/v1/parameters/{key}").build(key))
                .exchangeToMono(resp -> {
                    HttpStatusCode sc = resp.statusCode();
                    if (sc.is2xxSuccessful()) {
                        return resp.bodyToMono(ParameterDto.class);
                    }
                    if (sc.value() == HttpStatus.NOT_FOUND.value()) {
                        // 404 = no existe → no es error
                        log.debug("Parámetro '{}' no existe (404).", key);
                        return Mono.empty();
                    }
                    // Otros errores: log con cuerpo y propagar
                    return resp.bodyToMono(String.class).defaultIfEmpty("")
                            .flatMap(body -> {
                                log.error("HTTP {} al consultar parámetro '{}', body={}", sc.value(), key, body);
                                return Mono.error(new RuntimeException(
                                        "HTTP " + sc.value() + " consultando catálogo de parámetros"));
                            });
                })
                .block();

        if (dto == null) {
            // deja que el caller (controller) decida 404/fallback
            return null;
        }

        log.debug("Valor obtenido → {} = {}", dto.key(), dto.value());
        return dto.value();
    }

    @Override
    public String get(String code, Locale locale, Object... args) {
        // Hoy no manejas localización; si la agregas, aquí puedes enviar ?lang= o header.
        return get(code, args);
    }

    /** DTO esperado del service de parámetros */
    public record ParameterDto(String key, String value) {}
}
