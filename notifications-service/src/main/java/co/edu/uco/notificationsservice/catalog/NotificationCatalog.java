package co.edu.uco.notificationsservice.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class NotificationCatalog {

    private static final Map<String, NotificationTemplate> TEMPLATES = new ConcurrentHashMap<>();

    static {
        // Confirmación de email
        upsert(new NotificationTemplate(
                "USER_EMAIL_CONFIRMATION",
                "Envía un enlace para confirmar el correo del nuevo usuario",
                new NotificationTemplate.EmailTemplate(
                        "Confirma tu correo electrónico",
                        "<p>Hola {name},</p><p>Gracias por registrarte. Haz clic en el siguiente enlace para confirmar tu correo: {confirmationLink}</p>",
                        "Hola {name}, confirma tu correo ingresando a {confirmationLink}"
                ),
                null
        ));

        // Confirmación de celular vía SMS
        upsert(new NotificationTemplate(
                "USER_MOBILE_CONFIRMATION",
                "Solicita confirmar el número celular registrado",
                null,
                new NotificationTemplate.SmsTemplate(
                        "Hola {name}, confirma tu número ingresando el código {otp} antes de {expirationTime}."
                )
        ));

        // Duplicados de identificación (ID interno)
        upsert(templateEmail(
                "USER_ID_DUPLICATED_ADMIN_ALERT",
                "Notifica al administrador cuando el identificador interno ya existe",
                "Conflicto con identificador de usuario",
                "<p>Hola,</p><p>Se detectó un conflicto con el identificador {userId}. Se generará uno nuevo automáticamente.</p><p>Ejecutor: {executor}</p>",
                "Hola, se detectó un conflicto con el identificador {userId}. Ejecutado por {executor}."
        ));

        upsert(templateEmail(
                "USER_ID_DUPLICATED_EXECUTOR_ALERT",
                "Informa a quien ejecutó la transacción sobre el conflicto del identificador",
                "Se detectó un identificador duplicado",
                "<p>Hola {executorName},</p><p>El identificador {userId} ya estaba en uso. Se generó uno nuevo para continuar con el proceso.</p>",
                "Hola {executorName}, el identificador {userId} ya estaba en uso. Se generó uno nuevo."
        ));

        // Duplicados de tipo y número de documento
        upsert(templateEmail(
                "USER_DOCUMENT_DUPLICATED_ADMIN_ALERT",
                "Notifica al administrador cuando un documento ya existe",
                "Documento duplicado detectado",
                "<p>Hola,</p><p>Ya existe un usuario con el tipo {idType} y número {idNumber}. Se notificó al ejecutor.</p>",
                "Documento duplicado: tipo {idType} número {idNumber}."
        ));

        upsert(templateEmail(
                "USER_DOCUMENT_DUPLICATED_EXECUTOR_ALERT",
                "Informa al ejecutor que el documento ya está registrado",
                "Documento ya registrado",
                "<p>Hola {executorName},</p><p>Ya existe un usuario con el tipo {idType} y número {idNumber}. Debes informar al solicitante.</p>",
                "Hola {executorName}, ya existe un usuario con {idType} {idNumber}."
        ));

        // Duplicado de correo
        upsert(templateEmail(
                "USER_EMAIL_DUPLICATED_OWNER_ALERT",
                "Notifica al dueño del correo que ya está registrado",
                "Tu correo ya está vinculado a una cuenta",
                "<p>Hola,</p><p>El correo {email} ya está registrado en nuestro sistema. Si desconoces este registro comunícate con soporte.</p>",
                "Hola, el correo {email} ya está registrado en nuestro sistema."
        ));

        upsert(templateEmail(
                "USER_EMAIL_DUPLICATED_EXECUTOR_ALERT",
                "Informa al ejecutor que el correo ya existe",
                "Correo electrónico duplicado",
                "<p>Hola {executorName},</p><p>El correo {email} ya está asignado a otro usuario. Por favor, verifica con el solicitante.</p>",
                "Hola {executorName}, el correo {email} ya pertenece a otro usuario."
        ));

        // Duplicado de celular
        upsert(new NotificationTemplate(
                "USER_MOBILE_DUPLICATED_OWNER_ALERT",
                "Envía un SMS al dueño del número cuando ya está registrado",
                null,
                new NotificationTemplate.SmsTemplate(
                        "Hola, el número {mobileNumber} ya está registrado en nuestra plataforma. Si no reconoces la operación contáctanos."
                )
        ));

        upsert(templateEmail(
                "USER_MOBILE_DUPLICATED_EXECUTOR_ALERT",
                "Informa al ejecutor que el número celular ya existe",
                "Número celular ya registrado",
                "<p>Hola {executorName},</p><p>El número {mobileNumber} ya está asignado a otra cuenta. Por favor, contacta al titular.</p>",
                "Hola {executorName}, el número {mobileNumber} ya está asignado a otra cuenta."
        ));
    }

    private NotificationCatalog() {
    }

    public static Optional<NotificationTemplate> get(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(TEMPLATES.get(key));
    }

    public static List<NotificationTemplate> findBulk(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return List.of();
        }
        List<NotificationTemplate> out = new ArrayList<>();
        for (String key : keys) {
            var template = TEMPLATES.get(key);
            if (template != null) {
                out.add(template);
            }
        }
        return out;
    }

    public static Map<String, NotificationTemplate> getAll() {
        return Collections.unmodifiableMap(TEMPLATES);
    }

    public static void upsert(NotificationTemplate template) {
        if (template == null || template.getKey() == null || template.getKey().isBlank()) {
            throw new IllegalArgumentException("template and key are required");
        }
        TEMPLATES.put(template.getKey(), template);
    }

    public static boolean remove(String key) {
        return TEMPLATES.remove(key) != null;
    }

    public static boolean exists(String key) {
        return key != null && !key.isBlank() && TEMPLATES.containsKey(key);
    }

    private static NotificationTemplate templateEmail(String key,
                                                      String description,
                                                      String subject,
                                                      String html,
                                                      String text) {
        return new NotificationTemplate(
                key,
                description,
                new NotificationTemplate.EmailTemplate(subject, html, text),
                null
        );
    }
}
