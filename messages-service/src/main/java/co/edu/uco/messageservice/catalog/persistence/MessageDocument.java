package co.edu.uco.messageservice.catalog.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("message_templates")
public class MessageDocument {

    @Id
    private String key;
    private String template;

    public MessageDocument() {
    }

    public MessageDocument(String key, String template) {
        this.key = key;
        this.template = template;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}