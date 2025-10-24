package co.edu.uco.messageservice.catalog;

public class Message {

    private String key;    // p.ej. "USUARIO_CREADO_OK"
    private String text;   // p.ej. "Usuario creado exitosamente"
    private String locale; // p.ej. "es-CO"

    public Message() { }

    public Message(String key, String text) {
        this(key, text, "es-CO");
    }

    public Message(String key, String text, String locale) {
        setKey(key);
        setText(text);
        setLocale(locale);
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getLocale() { return locale; }
    public void setLocale(String locale) {
        this.locale = (locale == null || locale.isBlank()) ? "es-CO" : locale;
    }
}
