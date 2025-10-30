package co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.impl;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.notifications.NotificationCatalogPort;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.notifications.NotificationCommand;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.ExistingUserInformation;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.DuplicateRegistrationNotifier;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.event.DuplicateRegistrationEvent;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.event.DuplicateType;

@Component
public class NotificationDuplicateRegistrationNotifier implements DuplicateRegistrationNotifier {

    static final String ADMIN_EMAIL_PARAMETER = "USER.REGISTER.DUPLICATE.ADMIN_EMAIL";
    private static final Logger log = LoggerFactory.getLogger(NotificationDuplicateRegistrationNotifier.class);

    private final NotificationCatalogPort notificationCatalog;
    private final ParameterCatalogPort parameterCatalog;

    public NotificationDuplicateRegistrationNotifier(
            final NotificationCatalogPort notificationCatalog,
            final ParameterCatalogPort parameterCatalog) {
        this.notificationCatalog = notificationCatalog;
        this.parameterCatalog = parameterCatalog;
    }

    @Override
    public void notify(final DuplicateRegistrationEvent event) {
        if (event == null) {
            return;
        }

        logDuplicate(event);
        final var recipients = buildRecipients(event);
        if (recipients.isEmpty()) {
            return;
        }

        final var subject = "Intento de registro duplicado";
        final var body = buildBody(event);

        recipients.forEach(email -> notificationCatalog.send(
                new NotificationCommand(
                        templateKeyFor(event.type()),
                        email,
                        subject,
                        body)));
    }

    private void logDuplicate(final DuplicateRegistrationEvent event) {
        final var existing = event.existingUser();
        log.warn("Detección de duplicado en registro. tipo={}, candidato={}, existente={}", event.type(),
                event.candidate().email(),
                existing != null ? existing.email() : "N/A");
    }

    private Set<String> buildRecipients(final DuplicateRegistrationEvent event) {
        final Set<String> recipients = new LinkedHashSet<>();
        final var adminEmail = parameterCatalog.get(ADMIN_EMAIL_PARAMETER, Locale.getDefault());
        addRecipient(recipients, adminEmail);
        addRecipient(recipients, event.candidate().email());
        final ExistingUserInformation existing = event.existingUser();
        if (existing != null) {
            addRecipient(recipients, existing.email());
        }
        return recipients;
    }

    private void addRecipient(final Set<String> recipients, final String email) {
        final var sanitized = TextHelper.getDefaultWithTrim(email);
        if (!TextHelper.isEmpty(sanitized) && sanitized.contains("@")) {
            recipients.add(sanitized);
        }
    }

    private String buildBody(final DuplicateRegistrationEvent event) {
        final var candidate = event.candidate();
        final var existing = event.existingUser();
        final var existingName = existing != null ? existing.fullName() : "[desconocido]";
        final var existingEmail = existing != null ? existing.email() : "[desconocido]";

        return "Se detectó un intento de registro duplicado para " + event.type() +
                "\nSolicitante: " + candidate.email() +
                "\nTitular registrado: " + existingName + " - " + existingEmail;
    }

    private String templateKeyFor(final DuplicateType type) {
        return switch (type) {
            case IDENTIFICATION -> "user.register.duplicate.identification";
            case EMAIL -> "user.register.duplicate.email";
            case MOBILE_NUMBER -> "user.register.duplicate.mobile";
        };
    }
}