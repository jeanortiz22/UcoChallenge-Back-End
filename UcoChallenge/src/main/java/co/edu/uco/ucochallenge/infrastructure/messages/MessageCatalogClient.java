// infrastructure/messages/MessageCatalogClient.java
package co.edu.uco.ucochallenge.infrastructure.messages;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.crosscuting.messages.MessageCatalogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

@Component
public class MessageCatalogClient implements MessageCatalogPort {

    private static final Logger log = LoggerFactory.getLogger(MessageCatalogClient.class);
    private final WebClient messagesWebClient;

    public MessageCatalogClient(@Qualifier("messagesWebClient") WebClient messagesWebClient) {
        this.messagesWebClient = messagesWebClient;
    }

    @Override
    @Cacheable(
            cacheNames = "messages",
            // ¡OJO! usar #p1 (segundo parámetro) para evitar conflicto con la variable especial #args de SpEL
            key = "#code + '|' + T(java.util.Locale).getDefault().toLanguageTag() + '|' + T(java.util.Arrays).deepToString(#p1)",
            unless = "#result == null || #result.isBlank() || #result.startsWith('[')"
    )
    public String format(String code, Object... args) {
        try {
            log.debug("Resolviendo mensaje. code={}, args={}", code, Arrays.toString(args));

            var dto = messagesWebClient.get()
                    .uri("/{key}", code)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).defaultIfEmpty("")
                                    .flatMap(body -> {
                                        log.error("Catálogo respondió HTTP {} para code={}, body={}",
                                                response.statusCode().value(), code, body);
                                        return Mono.error(new UcoChallengeTechnicalException(
                                                "ERROR_CONSULTANDO_CATALOGO",
                                                "HTTP " + response.statusCode().value() + " al consultar el catálogo",
                                                (Throwable) null
                                        ));
                                    })
                    )
                    .bodyToMono(MessageDto.class)
                    .block();

            if (dto == null) {
                log.warn("DTO nulo desde catálogo para code={}", code);
                return "[" + code + "]";
            }
            log.debug("DTO recibido: key={}, template={}", dto.key(), dto.template());

            String template = (dto.template() == null || dto.template().isBlank())
                    ? "[" + code + "]"
                    : dto.template();

            try {
                return MessageFormat.format(template, args);
            } catch (IllegalArgumentException iae) {
                log.error("Error formateando plantilla: template='{}', args={}. {}",
                        template, Arrays.toString(args), iae.getMessage());
                return "[" + code + "]";
            }
        } catch (Exception e) {
            log.error("Fallo consultando catálogo para code={}: {}", code, e.getMessage());
            return "[APLICACION_NO_DISPONIBLE]";
        }
    }

    @Override
    public String format(String code, Locale locale, Object... args) {
        return format(code, args);
    }

    public record MessageDto(String key, String template) {}
}
