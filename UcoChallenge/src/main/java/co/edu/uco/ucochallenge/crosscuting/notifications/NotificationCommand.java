package co.edu.uco.ucochallenge.crosscuting.notifications;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

/**
 * Comando mínimo para enviar una notificación a través de NotificationAPI.
 */
public record NotificationCommand(
        String templateKey,
        String email,
        String subject,
        String htmlBody
) {

    public NotificationCommand {
        templateKey = TextHelper.getDefaultWithTrim(templateKey);
        email = TextHelper.getDefaultWithTrim(email);
        subject = TextHelper.getDefault(subject);
        htmlBody = TextHelper.getDefault(htmlBody);
    }

    public boolean hasTemplate() {
        return !TextHelper.isEmpty(templateKey);
    }

    public boolean hasEmail() {
        return !TextHelper.isEmpty(email);
    }

    public boolean hasSubject() {
        return !TextHelper.isEmpty(subject);
    }

    public boolean hasHtmlBody() {
        return !TextHelper.isEmpty(htmlBody);
    }
}
