package co.edu.uco.messageservice.catalog;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Message {
	private final String key;
    private final String template;

    @JsonCreator
    public Message(@JsonProperty("key") String key,
                   @JsonProperty("template") String template) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("key is required");
        }
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("template is required");
        }
        this.key = key.trim();
        this.template = template;
    }

    public String getKey() {
        return key;
    }

    public String getTemplate() {
        return template;
    }
}
