package co.edu.uco.ucochallenge.infrastructure.parameters;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.crosscuting.parameters.ParameterCatalogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Locale;

@Component
public class ParameterCatalogClient implements ParameterCatalogPort {

    private static final Logger log = LoggerFactory.getLogger(ParameterCatalogClient.class);
    private final WebClient parametersWebClient;

    public ParameterCatalogClient(@Qualifier("parametersWebClient") WebClient parametersWebClient) {
        this.parametersWebClient = parametersWebClient;
    }

    @Override
    @Cacheable(
            cacheNames = "parameters",
            key = "#code + '|' + T(java.util.Locale).getDefault().toLanguageTag() + '|' + T(java.util.Arrays).deepToString(#p1)",
            unless = "#result == null || #result.isBlank()"
    )
    public String get(String code, Object... args) {
        try {
            log.debug("Consultando parámetro. code={}, args={}", code, Arrays.toString(args));

            var dto = parametersWebClient.get()
                    .uri("/{key}", code)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).defaultIfEmpty("")
                                    .flatMap(body -> {
                                        log.error("Catálogo de parámetros respondió HTTP {} para code={}, body={}",
                                                response.statusCode().value(), code, body);
                                        return Mono.error(new UcoChallengeTechnicalException(
                                                "ERROR_CONSULTANDO_PARAMETROS",
                                                "HTTP " + response.statusCode().value() + " al consultar catálogo de parámetros",
                                                (Throwable) null
                                        ));
                                    })
                    )
                    .bodyToMono(ParameterDto.class)
                    .block();

            if (dto == null) {
                log.warn("DTO nulo desde catálogo de parámetros para code={}", code);
                return "[" + code + "]";
            }

            log.debug("DTO recibido: key={}, value={}", dto.key(), dto.value());
            return dto.value();

        } catch (Exception e) {
            log.error("Error consultando parámetro para code={}: {}", code, e.getMessage());
            return "[CATALOGO_PARAMETROS_NO_DISPONIBLE]";
        }
    }

    @Override
    public String get(String code, Locale locale, Object... args) {
        return get(code, args);
    }

    public record ParameterDto(String key, String value) {}
}
