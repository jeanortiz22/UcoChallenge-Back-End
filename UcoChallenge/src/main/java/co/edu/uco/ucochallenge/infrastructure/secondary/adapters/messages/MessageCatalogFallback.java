package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.messages;

import java.text.MessageFormat;
import java.util.Map;

public final class MessageCatalogFallback {

    private static final Map<String, String> DEFAULT_TEMPLATES = Map.ofEntries(
            Map.entry("user.confirmation.input.data.required", "Faltan datos para procesar la confirmación."),
            Map.entry("user.confirmation.user.not.found", "No encontramos al usuario solicitado."),
            Map.entry("user.confirmation.email.not.available", "El usuario no tiene correo electrónico registrado."),
            Map.entry("user.confirmation.mobile.not.available", "El usuario no tiene teléfono móvil registrado."),
            Map.entry("user.confirmation.email.sent", "Enviamos un código de verificación a tu correo."),
            Map.entry("user.confirmation.mobile.sent", "Enviamos un código de verificación a tu teléfono."),
            Map.entry("user.confirmation.email.already.confirmed", "El correo electrónico ya estaba verificado."),
            Map.entry("user.confirmation.mobile.already.confirmed", "El teléfono móvil ya estaba verificado."),
            Map.entry("user.confirmation.email.confirmed", "Correo electrónico verificado correctamente."),
            Map.entry("user.confirmation.mobile.confirmed", "Teléfono móvil verificado correctamente."),
            Map.entry("user.confirmation.token.required", "Debes proporcionar el código de verificación."),
            Map.entry("user.confirmation.token.invalid", "El código de verificación es inválido."),
            Map.entry("user.confirmation.token.expired", "El código de verificación expiró."));

    private MessageCatalogFallback() {
    }

    static String format(final String code, final Object... args) {
        final var template = DEFAULT_TEMPLATES.get(code);
        if (template == null || template.isBlank()) {
            return null;
        }
        try {
            return MessageFormat.format(template, args);
        } catch (IllegalArgumentException ex) {
            return template;
        }
    }

    public static boolean isFallback(final String code, final Object[] args, final String message) {
        if (message == null || message.isBlank()) {
            return true;
        }
        if (message.startsWith("[")) {
            return true;
        }
        final var expected = format(code, args);
        return expected != null && expected.equals(message);
    }
}