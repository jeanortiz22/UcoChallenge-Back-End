package co.edu.uco.messageservice.catalog;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MessageCatalog {

    private static final Map<String, Message> MESSAGES = new HashMap<>();

    private MessageCatalog() { }

    static {
        // Semillas de ejemplo
        MESSAGES.put("USUARIO_CREADO_OK", new Message("USUARIO_CREADO_OK", "Usuario creado exitosamente", "es-CO"));
        MESSAGES.put("USUARIO_YA_EXISTE", new Message("USUARIO_YA_EXISTE", "El usuario ya existe", "es-CO"));
        MESSAGES.put("PARAMETRO_NO_ENCONTRADO", new Message("PARAMETRO_NO_ENCONTRADO", "El parámetro solicitado no fue encontrado", "es-CO"));

        // Inglés (ejemplo)
        MESSAGES.put("USER_CREATED_OK_EN", new Message("USER_CREATED_OK_EN", "User created successfully", "en-US"));
    }

    public static Optional<Message> get(String key) {
        return Optional.ofNullable(MESSAGES.get(key));
    }

    public static Map<String, Message> getAll() {
        return MESSAGES;
    }

    public static void upsert(Message message) {
        if (message == null || message.getKey() == null || message.getKey().isBlank()) return;
        MESSAGES.put(message.getKey(), message);
    }

    public static void remove(String key) {
        MESSAGES.remove(key);
    }
}
