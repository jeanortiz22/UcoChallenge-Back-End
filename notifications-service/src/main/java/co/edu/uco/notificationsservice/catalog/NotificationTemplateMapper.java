package co.edu.uco.notificationsservice.catalog;

import co.edu.uco.notificationsservice.catalog.NotificationTemplate.EmailTemplate;
import co.edu.uco.notificationsservice.catalog.NotificationTemplate.SmsTemplate;
import co.edu.uco.notificationsservice.catalog.persistence.NotificationTemplateDocument;
import co.edu.uco.notificationsservice.catalog.persistence.NotificationTemplateDocument.EmailTemplateDocument;
import co.edu.uco.notificationsservice.catalog.persistence.NotificationTemplateDocument.SmsTemplateDocument;

final class NotificationTemplateMapper {

    private NotificationTemplateMapper() {
    }

    static NotificationTemplate toDomain(NotificationTemplateDocument document) {
        if (document == null) {
            return null;
        }
        return new NotificationTemplate(
                document.getKey(),
                document.getDescription(),
                toEmail(document.getEmail()),
                toSms(document.getSms())
        );
    }

    static NotificationTemplateDocument toDocument(NotificationTemplate template) {
        if (template == null) {
            return null;
        }
        return new NotificationTemplateDocument(
                template.getKey(),
                template.getDescription(),
                toEmailDocument(template.getEmail()),
                toSmsDocument(template.getSms())
        );
    }

    private static EmailTemplate toEmail(EmailTemplateDocument document) {
        if (document == null) {
            return null;
        }
        return new EmailTemplate(document.getSubject(), document.getHtmlBody(), document.getTextBody());
    }

    private static EmailTemplateDocument toEmailDocument(EmailTemplate template) {
        if (template == null) {
            return null;
        }
        return new EmailTemplateDocument(template.subject(), template.htmlBody(), template.textBody());
    }

    private static SmsTemplate toSms(SmsTemplateDocument document) {
        if (document == null) {
            return null;
        }
        return new SmsTemplate(document.getText());
    }

    private static SmsTemplateDocument toSmsDocument(SmsTemplate template) {
        if (template == null) {
            return null;
        }
        return new SmsTemplateDocument(template.text());
    }
}