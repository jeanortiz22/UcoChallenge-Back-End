// Message.java
package co.edu.uco.messageservice.catalog;

public final class Message {
    private final String key;       // p.ej. "EMAIL_TAKEN"
    private final String template;  // p.ej. "El correo {0} ya está registrado."

    public Message(String key, String template) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("key is required");
        if (template == null || template.isBlank()) throw new IllegalArgumentException("template is required");
        this.key = key.trim();
        this.template = template;
    }

    public String getKey() { return key; }
    public String getTemplate() { return template; }
}
