package co.edu.uco.notificationsservice.catalog;

import java.util.ArrayList;
import java.util.List;

final class NotificationCatalogDefaults {

    private NotificationCatalogDefaults() {
    }

    static List<NotificationTemplate> defaults() {
        List<NotificationTemplate> templates = new ArrayList<>();

        // Confirmaciones
        templates.add(new NotificationTemplate(
                "user.register.confirmation",
                "Plantilla genérica para confirmación de registro",
                new NotificationTemplate.EmailTemplate(
                        "Confirma tu registro",
                        "<p>Hola {name}, utiliza el código enviado para confirmar tu registro.</p>",
                        "Hola {name}, utiliza el código enviado para confirmar tu registro."),
                new NotificationTemplate.SmsTemplate(
                        "Tu código de confirmación está disponible en este mensaje.")));

        templates.add(new NotificationTemplate(
                "USER_EMAIL_CONFIRMATION",
                "Envía un enlace para confirmar el correo del nuevo usuario",
                new NotificationTemplate.EmailTemplate(
                        "Confirma tu correo electrónico",
                        "<p>Hola {name},</p><p>Gracias por registrarte. Haz clic en el siguiente enlace para confirmar tu correo: {confirmationLink}</p>",
                        "Hola {name}, confirma tu correo ingresando a {confirmationLink}"),
                null));

        templates.add(new NotificationTemplate(
                "USER_MOBILE_CONFIRMATION",
                "Solicita confirmar el número celular registrado",
                null,
                new NotificationTemplate.SmsTemplate(
                        "Hola {name}, confirma tu número ingresando el código {otp} antes de {expirationTime}.")));

        // Duplicados (nuevos identificadores en minúscula usados por el dominio)
        templates.add(new NotificationTemplate(
                "user.register.duplicate.identification",
                "Alerta cuando el identificador interno ya existe",
                new NotificationTemplate.EmailTemplate(
                        "Conflicto con identificador de usuario",
                        "<p>Se detectó un intento de registro con un identificador existente.</p>",
                        "Se detectó un intento de registro con un identificador existente."),
                null));

        templates.add(new NotificationTemplate(
                "user.register.duplicate.email",
                "Alerta cuando un correo ya está registrado",
                new NotificationTemplate.EmailTemplate(
                        "Correo electrónico ya registrado",
                        "<p>El correo proporcionado ya está asociado a otra cuenta.</p>",
                        "El correo proporcionado ya está asociado a otra cuenta."),
                null));

        templates.add(new NotificationTemplate(
                "user.register.duplicate.mobile",
                "Alerta cuando un número celular ya está registrado",
                new NotificationTemplate.EmailTemplate(
                        "Número de celular registrado",
                        "<p>El número de celular proporcionado ya está vinculado a otra cuenta.</p>",
                        "El número de celular proporcionado ya está vinculado a otra cuenta."),
                new NotificationTemplate.SmsTemplate(
                        "El número de celular proporcionado ya está vinculado a otra cuenta.")));

        // Plantillas heredadas (mayúsculas) para compatibilidad
        templates.add(templateEmail(
                "USER_ID_DUPLICATED_ADMIN_ALERT",
                "Notifica al administrador cuando el identificador interno ya existe",
                "Conflicto con identificador de usuario",
                "<p>Hola,</p><p>Se detectó un conflicto con el identificador {userId}. Se generará uno nuevo automáticamente.</p><p>Ejecutor: {executor}</p>",
                "Hola, se detectó un conflicto con el identificador {userId}. Ejecutado por {executor}."));

        templates.add(templateEmail(
                "USER_ID_DUPLICATED_EXECUTOR_ALERT",
                "Informa a quien ejecutó la transacción sobre el conflicto del identificador",
                "Se detectó un identificador duplicado",
                "<p>Hola {executorName},</p><p>El identificador {userId} ya estaba en uso. Se generó uno nuevo para continuar con el proceso.</p>",
                "Hola {executorName}, el identificador {userId} ya estaba en uso. Se generó uno nuevo."));

        templates.add(templateEmail(
                "USER_DOCUMENT_DUPLICATED_ADMIN_ALERT",
                "Notifica al administrador cuando un documento ya existe",
                "Documento duplicado detectado",
                "<p>Hola,</p><p>Ya existe un usuario con el tipo {idType} y número {idNumber}. Se notificó al ejecutor.</p>",
                "Documento duplicado: tipo {idType} número {idNumber}."));

        templates.add(templateEmail(
                "USER_DOCUMENT_DUPLICATED_EXECUTOR_ALERT",
                "Informa al ejecutor que el documento ya está registrado",
                "Documento ya registrado",
                "<p>Hola {executorName},</p><p>Ya existe un usuario con el tipo {idType} y número {idNumber}. Debes informar al solicitante.</p>",
                "Hola {executorName}, ya existe un usuario con {idType} {idNumber}."));

        templates.add(templateEmail(
                "USER_EMAIL_DUPLICATED_OWNER_ALERT",
                "Notifica al dueño del correo que ya está registrado",
                "Tu correo ya está vinculado a una cuenta",
                "<p>Hola,</p><p>El correo {email} ya está registrado en nuestro sistema. Si desconoces este registro comunícate con soporte.</p>",
                "Hola, el correo {email} ya está registrado en nuestro sistema."));

        templates.add(templateEmail(
                "USER_EMAIL_DUPLICATED_EXECUTOR_ALERT",
                "Informa al ejecutor que el correo ya existe",
                "Correo electrónico duplicado",
                "<p>Hola {executorName},</p><p>El correo {email} ya está asignado a otro usuario. Por favor, verifica con el solicitante.</p>",
                "Hola {executorName}, el correo {email} ya pertenece a otro usuario."));

        templates.add(new NotificationTemplate(
                "USER_MOBILE_DUPLICATED_OWNER_ALERT",
                "Envía un SMS al dueño del número cuando ya está registrado",
                null,
                new NotificationTemplate.SmsTemplate(
                        "Hola, el número {mobileNumber} ya está registrado en nuestra plataforma. Si no reconoces la operación contáctanos.")));

        templates.add(templateEmail(
                "USER_MOBILE_DUPLICATED_EXECUTOR_ALERT",
                "Informa al ejecutor que el número celular ya existe",
                "Número celular ya registrado",
                "<p>Hola {executorName},</p><p>El número {mobileNumber} ya está asignado a otra cuenta. Por favor, contacta al titular.</p>",
                "Hola {executorName}, el número {mobileNumber} ya está asignado a otra cuenta."));

        return templates;
    }

    private static NotificationTemplate templateEmail(String key, String description, String subject, String html, String text) {
        return new NotificationTemplate(key, description,
                new NotificationTemplate.EmailTemplate(subject, html, text),
                null);
    }
}