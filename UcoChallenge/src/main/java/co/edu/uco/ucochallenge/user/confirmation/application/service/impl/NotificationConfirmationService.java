package co.edu.uco.ucochallenge.user.confirmation.application.service.impl;

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
import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.service.ConfirmationNotificationService;
import co.edu.uco.ucochallenge.user.shared.MobileNumberFormatter;

@Component
public class NotificationConfirmationService implements ConfirmationNotificationService {

    static final String TEMPLATE_KEY = "user.register.confirmation";
    static final String EMAIL_SUBJECT_CODE = "user.confirmation.email.subject";
    static final String EMAIL_BODY_CODE = "user.confirmation.email.body";
    static final String SMS_MESSAGE_CODE = "user.confirmation.mobile.body";

    private static final Logger log = LoggerFactory.getLogger(NotificationConfirmationService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withLocale(Locale.getDefault());

    private final NotificationCatalogPort notificationCatalog;
    private final MessageCatalogPort messageCatalog;
    private final MobileNumberFormatter mobileNumberFormatter;

    public NotificationConfirmationService(
            final NotificationCatalogPort notificationCatalog,
            final MessageCatalogPort messageCatalog,
            final MobileNumberFormatter mobileNumberFormatter) {
        this.notificationCatalog = notificationCatalog;
        this.messageCatalog = messageCatalog;
        this.mobileNumberFormatter = mobileNumberFormatter;
    }

    @Override
    public void sendEmailConfirmation(final UserConfirmationDomain user) {
        if (ObjectHelper.isNull(user) || TextHelper.isEmpty(user.email())) {
            return;
        }
        if (TextHelper.isEmpty(user.emailConfirmationToken())) {
            log.debug("Usuario sin token de confirmación de correo. Se omite notificación. userId={}", user.id());
            return;
        }

        try {
            final var subject = resolveMessage(EMAIL_SUBJECT_CODE,
                    () -> "Confirma tu correo en UCO Challenge",
                    user.displayName());
            final var body = resolveMessage(EMAIL_BODY_CODE,
                    () -> defaultEmailBody(user),
                    user.displayName(),
                    user.emailConfirmationToken(),
                    formatDate(user.emailConfirmationExpiresAt()));

            final var command = new NotificationCommand(
                    TEMPLATE_KEY,
                    user.email(),
                    subject,
                    body,
                    TextHelper.getDefault(),
                    TextHelper.getDefault());

            notificationCatalog.send(command);
        } catch (Exception ex) {
            log.error("Error enviando notificación de confirmación de correo para userId={}: {}",
                    user.id(), ex.getMessage(), ex);
        }
    }

    @Override
    public void sendMobileConfirmation(final UserConfirmationDomain user) {
        if (ObjectHelper.isNull(user) || TextHelper.isEmpty(user.mobileNumber())) {
            return;
        }
        if (TextHelper.isEmpty(user.mobileConfirmationToken())) {
            log.debug("Usuario sin token de confirmación móvil. Se omite notificación. userId={}", user.id());
            return;
        }

        try {
            final var smsMessage = resolveMessage(SMS_MESSAGE_CODE,
                    () -> defaultSmsMessage(user),
                    user.mobileConfirmationToken(),
                    formatDate(user.mobileConfirmationExpiresAt()));

            final var command = new NotificationCommand(
                    TEMPLATE_KEY,
                    TextHelper.getDefault(),
                    TextHelper.getDefault(),
                    TextHelper.getDefault(),
                    mobileNumberFormatter.format(user.mobileNumber()),
                    smsMessage);

            notificationCatalog.send(command);
        } catch (Exception ex) {
            log.error("Error enviando notificación de confirmación móvil para userId={}: {}",
                    user.id(), ex.getMessage(), ex);
        }
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

    private String defaultEmailBody(final UserConfirmationDomain user) {
        final var greetingName = TextHelper.isEmpty(user.displayName()) ? "Hola" : user.displayName();
        final var emailToken = TextHelper.getDefaultWithTrim(user.emailConfirmationToken());
        final var emailExpiry = formatDate(user.emailConfirmationExpiresAt());

        final var builder = new StringBuilder(greetingName)
                .append(", confirma tu dirección de correo electrónico.")
                .append(System.lineSeparator());

        builder.append("Código de confirmación: ")
                .append(emailToken);
        if (!TextHelper.isEmpty(emailExpiry)) {
            builder.append(" (vigente hasta ").append(emailExpiry).append(")");
        }
        builder.append(System.lineSeparator())
                .append("Si no solicitaste esta cuenta, ignora este mensaje.");

        return builder.toString();
    }

    private String defaultSmsMessage(final UserConfirmationDomain user) {
        final var token = TextHelper.getDefaultWithTrim(user.mobileConfirmationToken());
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