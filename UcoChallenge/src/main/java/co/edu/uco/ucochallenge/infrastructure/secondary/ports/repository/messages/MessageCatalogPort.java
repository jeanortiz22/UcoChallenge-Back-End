// crosscuting/messages/MessageCatalogPort.java
package co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.messages;

import java.util.Locale;

public interface MessageCatalogPort {
    /**
     * Resuelve y formatea el mensaje para el código dado.
     * Si no existe, debería devolver "[CODE]" como marcador.
     */
    String format(String code, Object... args);

    /**
     * Variante con locale (útil para i18n). Si no usas i18n ahora,
     * puedes pasar Locale.getDefault() internamente.
     */
    default String format(String code, Locale locale, Object... args) {
        return format(code, args);
    }
}
