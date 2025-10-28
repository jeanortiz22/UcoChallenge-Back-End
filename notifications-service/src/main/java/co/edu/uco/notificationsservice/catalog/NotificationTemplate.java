package co.edu.uco.notificationsservice.catalog;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class NotificationTemplate {

    private final String key;
    private final String description;
    private final EmailTemplate email;
    private final SmsTemplate sms;

    @JsonCreator
    public NotificationTemplate(
            @JsonProperty("key") String key,
            @JsonProperty("description") String description,
            @JsonProperty("email") EmailTemplate email,
            @JsonProperty("sms") SmsTemplate sms) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("key is required");
        }
        this.key = key.trim();
        this.description = description == null ? "" : description.trim();
        this.email = email;
        this.sms = sms;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public EmailTemplate getEmail() {
        return email;
    }

    public SmsTemplate getSms() {
        return sms;
    }

    public boolean hasEmail() {
        return email != null;
    }

    public boolean hasSms() {
        return sms != null;
    }

    public record EmailTemplate(String subject, String htmlBody, String textBody) {
        @JsonCreator
        public EmailTemplate(@JsonProperty("subject") String subject,
                             @JsonProperty("htmlBody") String htmlBody,
                             @JsonProperty("textBody") String textBody) {
            if ((subject == null || subject.isBlank()) && (htmlBody == null || htmlBody.isBlank())
                    && (textBody == null || textBody.isBlank())) {
                throw new IllegalArgumentException("At least one email content attribute is required");
            }
            this.subject = subject;
            this.htmlBody = htmlBody;
            this.textBody = textBody;
        }
    }

    public record SmsTemplate(String text) {
        @JsonCreator
        public SmsTemplate(@JsonProperty("text") String text) {
            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException("text is required");
            }
            this.text = text;
        }
    }
}