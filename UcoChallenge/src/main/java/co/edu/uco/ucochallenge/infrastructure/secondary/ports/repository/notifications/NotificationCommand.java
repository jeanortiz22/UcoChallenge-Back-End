package co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.notifications;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

public record NotificationCommand(
        String templateKey,
        String email,
        String subject,
        String htmlBody,
        String mobileNumber,
        String smsMessage
) {

    public NotificationCommand {
        templateKey = TextHelper.getDefaultWithTrim(templateKey);
        email = TextHelper.getDefaultWithTrim(email);
        subject = TextHelper.getDefault(subject);
        htmlBody = TextHelper.getDefault(htmlBody);
        mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);
        smsMessage = TextHelper.getDefaultWithTrim(smsMessage);
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
    
    public boolean hasMobileNumber() {
        return !TextHelper.isEmpty(mobileNumber);
    }

    public boolean hasSmsMessage() {
        return !TextHelper.isEmpty(smsMessage);
    }
}
