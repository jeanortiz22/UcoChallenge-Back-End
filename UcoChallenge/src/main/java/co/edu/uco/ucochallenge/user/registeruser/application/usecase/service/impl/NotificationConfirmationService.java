package co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.messages.MessageCatalogPort;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.notifications.NotificationCatalogPort;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.notifications.NotificationCommand;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.ConfirmationNotificationService;

@Component
public class NotificationConfirmationService implements ConfirmationNotificationService {

    static final String TEMPLATE_KEY = "user.register.confirmation";
    static final String EMAIL_SUBJECT_CODE = "user.register.confirmation.email.subject";
    static final String EMAIL_BODY_CODE = "user.register.confirmation.email.body";
    static final String SMS_MESSAGE_CODE = "user.register.confirmation.sms";

    private static final Logger log = LoggerFactory.getLogger(NotificationConfirmationService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withLocale(Locale.getDefault());

    private final NotificationCatalogPort notificationCatalog;
    private final MessageCatalogPort messageCatalog;

    public NotificationConfirmationService(
            final NotificationCatalogPort notificationCatalog,
            final MessageCatalogPort messageCatalog) {
        this.notificationCatalog = notificationCatalog;
        this.messageCatalog = messageCatalog;
    }

    @Override
    public void notify(final RegisterUserDomain user) {
        if (ObjectHelper.isNull(user)) {
            return;
        }

        if (!hasContactInformation(user)) {
            log.debug("Usuario sin datos de contacto para notificar confirmación. userId={}", user.id());
            return;
        }

        if (TextHelper.isEmpty(user.mobileConfirmationToken())) {
            log.debug("Usuario sin token de confirmación móvil. Se omite notificación SMS. userId={}", user.id());
        }

        try {
            final var subject = resolveMessage(EMAIL_SUBJECT_CODE,
                    () -> "Confirma tu cuenta en UCO Challenge");
            final var emailBody = resolveMessage(EMAIL_BODY_CODE,
                    () -> defaultEmailBody(user),
                    user.firstName(),
                    user.emailConfirmationToken(),
                    formatDate(user.emailConfirmationExpiresAt()),
                    user.mobileConfirmationToken(),
                    formatDate(user.mobileConfirmationExpiresAt()));
            final var smsMessage = resolveMessage(SMS_MESSAGE_CODE,
                    () -> defaultSmsMessage(user),
                    user.mobileConfirmationToken(),
                    formatDate(user.mobileConfirmationExpiresAt()));

            final var command = new NotificationCommand(
                    TEMPLATE_KEY,
                    user.email(),
                    subject,
                    emailBody,
                    user.mobileNumber(),
                    smsMessage);

            notificationCatalog.send(command);
        } catch (Exception ex) {
            log.error("Error enviando notificación de confirmación para userId={}: {}",
                    user.id(), ex.getMessage(), ex);
        }
    }

    private boolean hasContactInformation(final RegisterUserDomain user) {
        return !TextHelper.isEmpty(user.email()) || !TextHelper.isEmpty(user.mobileNumber());
    }

    private String resolveMessage(final String code, final java.util.function.Supplier<String> fallback,
            final Object... args) {
        try {
            final var message = messageCatalog.format(code, args);
            if (TextHelper.isEmpty(message) || message.startsWith("[")) {
                return fallback.get();
            }
            return message;
        } catch (Exception ex) {
            log.debug("No se pudo resolver mensaje {} desde catálogo: {}", code, ex.getMessage());
            return fallback.get();
        }
    }

    private String defaultEmailBody(final RegisterUserDomain user) {
        final var greetingName = TextHelper.isEmpty(user.firstName()) ? "Hola" : user.firstName();
        final var emailToken = TextHelper.getDefaultWithTrim(user.emailConfirmationToken());
        final var emailExpiry = formatDate(user.emailConfirmationExpiresAt());
        final var smsToken = TextHelper.getDefaultWithTrim(user.mobileConfirmationToken());
        final var smsExpiry = formatDate(user.mobileConfirmationExpiresAt());

        final var builder = new StringBuilder(greetingName)
                .append(", completa la activación de tu cuenta.")
                .append(System.lineSeparator());

        if (!TextHelper.isEmpty(emailToken)) {
            builder.append("Código de confirmación de correo: ")
                    .append(emailToken);
            if (!TextHelper.isEmpty(emailExpiry)) {
                builder.append(" (vigente hasta ").append(emailExpiry).append(")");
            }
            builder.append(System.lineSeparator());
        }

        if (!TextHelper.isEmpty(smsToken)) {
            builder.append("Código de confirmación de teléfono: ")
                    .append(smsToken);
            if (!TextHelper.isEmpty(smsExpiry)) {
                builder.append(" (vigente hasta ").append(smsExpiry).append(")");
            }
            builder.append(System.lineSeparator());
        }

        builder.append("Si no solicitaste esta cuenta, ignora este mensaje.");
        return builder.toString();
    }

    private String defaultSmsMessage(final RegisterUserDomain user) {
        final var token = TextHelper.getDefaultWithTrim(user.mobileConfirmationToken());
        if (TextHelper.isEmpty(token)) {
            return TextHelper.getDefault();
        }
        final var expiry = formatDate(user.mobileConfirmationExpiresAt());
        final var builder = new StringBuilder("Tu código de verificación es: ")
                .append(token);
        if (!TextHelper.isEmpty(expiry)) {
            builder.append(" (válido hasta ").append(expiry).append(")");
        }
        builder.append(". No compartas este código.");
        return builder.toString();
    }

    private String formatDate(final LocalDateTime dateTime) {
        if (ObjectHelper.isNull(dateTime) || LocalDateTime.MIN.equals(dateTime)) {
            return TextHelper.getDefault();
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }
}