package co.edu.uco.ucochallenge.infrastructure.security.messages;

import co.edu.uco.ucochallenge.crosscuting.messages.MessageCatalogPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MessageCatalogClient implements MessageCatalogPort {

    private final WebClient messagesWebClient;

    public MessageCatalogClient(WebClient messagesWebClient) {
        this.messagesWebClient = messagesWebClient;
    }

    @Override
    @Cacheable(cacheNames = "messages", key = "#key",
               unless = "#result == null || #result.isBlank() || #result.startsWith(\"[\")")
    public String getTemplate(String key) {
        try {
            var dto = messagesWebClient.get()
                    .uri("/{key}", key)
                    .retrieve()
                    .bodyToMono(MessageDto.class)
                    .block();

            if (dto == null || dto.template() == null || dto.template().isBlank()) {
                return "[" + key + "]"; // marcamos no-encontrado (no se cachea por 'unless')
            }
            return dto.template();
        } catch (Exception e) {
            return "[APLICACION_NO_DISPONIBLE]"; // tampoco se cachea por 'unless'
        }
    }

    public record MessageDto(String key, String template) {}
}
