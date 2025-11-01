package co.edu.uco.notificationsservice.catalog.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("notification_templates")
public class NotificationTemplateDocument {

    @Id
    private String key;
    private String description;
    private EmailTemplateDocument email;
    private SmsTemplateDocument sms;

    public NotificationTemplateDocument() {
    }

    public NotificationTemplateDocument(String key, String description,
                                        EmailTemplateDocument email,
                                        SmsTemplateDocument sms) {
        this.key = key;
        this.description = description;
        this.email = email;
        this.sms = sms;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmailTemplateDocument getEmail() {
        return email;
    }

    public void setEmail(EmailTemplateDocument email) {
        this.email = email;
    }

    public SmsTemplateDocument getSms() {
        return sms;
    }

    public void setSms(SmsTemplateDocument sms) {
        this.sms = sms;
    }

    public static class EmailTemplateDocument {
        private String subject;
        private String htmlBody;
        private String textBody;

        public EmailTemplateDocument() {
        }

        public EmailTemplateDocument(String subject, String htmlBody, String textBody) {
            this.subject = subject;
            this.htmlBody = htmlBody;
            this.textBody = textBody;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getHtmlBody() {
            return htmlBody;
        }

        public void setHtmlBody(String htmlBody) {
            this.htmlBody = htmlBody;
        }

        public String getTextBody() {
            return textBody;
        }

        public void setTextBody(String textBody) {
            this.textBody = textBody;
        }
    }

    public static class SmsTemplateDocument {
        private String text;

        public SmsTemplateDocument() {
        }

        public SmsTemplateDocument(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}