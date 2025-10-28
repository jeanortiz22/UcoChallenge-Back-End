package co.edu.uco.ucochallenge.infrastructure.config;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import com.notificationapi.NotificationApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationApiConfig {

    private static final Logger log = LoggerFactory.getLogger(NotificationApiConfig.class);

    @Bean
    public NotificationApi notificationApi(
            @Value("${notification.api.client-id}") String clientId,
            @Value("${notification.api.client-secret}") String clientSecret
    ) {
        String normalizedClientId = TextHelper.getDefaultWithTrim(clientId);
        String normalizedClientSecret = TextHelper.getDefaultWithTrim(clientSecret);

        if (TextHelper.isEmpty(normalizedClientId) || TextHelper.isEmpty(normalizedClientSecret)) {
            throw UcoChallengeTechnicalException.create(
                    "CREDENCIALES_NOTIFICACION_INVALIDAS",
                    "Las credenciales de NotificationAPI son obligatorias"
            );
        }

        log.info("### NotificationAPI configurada. clientIdLength={}", normalizedClientId.length());
        return new NotificationApi(normalizedClientId, normalizedClientSecret);
    }
}