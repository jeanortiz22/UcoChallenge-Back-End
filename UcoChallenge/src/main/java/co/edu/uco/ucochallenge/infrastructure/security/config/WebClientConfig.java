package co.edu.uco.ucochallenge.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient messagesWebClient(WebClient.Builder builder) {
        // Alinea con el controller del message-service:
        return builder.baseUrl("http://localhost:8083/messages/api/v1/messages").build();
        // Si tu controller fuera "/messages/api/v1/messages": usa esa base URL aquí.
    }
}
