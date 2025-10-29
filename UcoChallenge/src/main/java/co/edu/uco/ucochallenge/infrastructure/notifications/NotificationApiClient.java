package co.edu.uco.ucochallenge.infrastructure.notifications;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.crosscuting.notifications.NotificationCatalogPort;
import co.edu.uco.ucochallenge.crosscuting.notifications.NotificationCommand;
import com.notificationapi.NotificationApi;
import com.notificationapi.model.EmailOptions;
import com.notificationapi.model.NotificationRequest;
import com.notificationapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationApiClient implements NotificationCatalogPort {

    private static final Logger log = LoggerFactory.getLogger(NotificationApiClient.class);
    private final NotificationApi notificationApi;

    public NotificationApiClient(NotificationApi notificationApi) {
        this.notificationApi = notificationApi;
    }

    @Override
    public String send(NotificationCommand command) {
        if (command == null) {
            throw UcoChallengeTechnicalException.create(
                    "NOTIFICACION_COMANDO_NULO",
                    "El comando de notificación es obligatorio"
            );
        }

        if (!command.hasTemplate()) {
            throw UcoChallengeTechnicalException.create(
                    "NOTIFICACION_PLANTILLA_OBLIGATORIA",
                    "El identificador de plantilla de notificación es obligatorio"
            );
        }

        if (!command.hasEmail()) {
            throw UcoChallengeTechnicalException.create(
                    "NOTIFICACION_DESTINATARIO_REQUERIDO",
                    "Debe proporcionarse al menos un email de destinatario para la notificación"
            );
        }

        try {
            User user = new User(command.email()).setEmail(command.email());
            NotificationRequest request = new NotificationRequest(command.templateKey(), user);

            if (command.hasSubject() || command.hasHtmlBody()) {
                EmailOptions emailOptions = new EmailOptions();
                if (command.hasSubject()) {
                    emailOptions.setSubject(command.subject());
                }
                if (command.hasHtmlBody()) {
                    emailOptions.setHtml(command.htmlBody());
                }
                request.setEmail(emailOptions);
            }

            String response = notificationApi.send(request);
            log.debug("Notificación enviada. template={}, email={}, respuesta={}",
                    command.templateKey(), command.email(), response);
            return response;
        } catch (Exception ex) {
            log.error("Error enviando notificación template={} email={}: {}",
                    command.templateKey(), command.email(), ex.getMessage(), ex);
            throw UcoChallengeTechnicalException.create(
                    "ERROR_ENVIANDO_NOTIFICACION",
                    "Error enviando la notificación a NotificationAPI",
                    ex
            );
        }
    }
}
