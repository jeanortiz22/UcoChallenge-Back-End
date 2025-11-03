package co.edu.uco.messageservice.catalog;

import java.util.ArrayList;
import java.util.List;

final class MessageCatalogDefaults {

    private MessageCatalogDefaults() {
    }

    static List<Message> defaults() {
        List<Message> messages = new ArrayList<>();

        // =========================
        // ✅ Éxito
        // =========================
        messages.add(new Message("USUARIO_CREADO_OK", "Usuario creado exitosamente."));
        messages.add(new Message("USUARIO_ACTUALIZADO_OK", "Usuario actualizado correctamente."));
        messages.add(new Message("USUARIO_ELIMINADO_OK", "Usuario eliminado correctamente."));
        messages.add(new Message("PARAMETRO_CREADO_OK", "Parámetro creado exitosamente."));
        messages.add(new Message("PARAMETRO_ACTUALIZADO_OK", "Parámetro actualizado correctamente."));
        messages.add(new Message("OPERACION_EXITOSA", "La operación se realizó correctamente."));
        messages.add(new Message("DATOS_GUARDADOS_OK", "Los datos fueron guardados correctamente."));

        // Mantener compatibilidad con mensajes existentes
        messages.add(new Message("user.register.email.duplicate", "El correo {0} ya está registrado."));

        // =========================
        // 🆕 Registro de Usuario (completo)
        // =========================

        // --- Input / Agregado ---
        messages.add(new Message("user.register.input.required",
                "Debes proporcionar los datos de entrada."));
        messages.add(new Message("user.register.input.domain.required",
                "El objeto de entrada para el registro de usuario es obligatorio."));
        messages.add(new Message("user.register.domain.identifier.required",
                "El identificador del agregado de usuario es obligatorio."));
        messages.add(new Message("user.register.domain.identifier.generation.failed",
                "No fue posible generar el identificador del usuario."));

        // --- idType ---
        messages.add(new Message("user.register.idType.required",
                "Debes seleccionar un tipo de identificación."));

        // --- idNumber ---
        messages.add(new Message("user.register.idNumber.required",
                "El campo 'Número de identificación' es obligatorio."));
        messages.add(new Message("user.register.idNumber.too.short",
                "El número de identificación debe tener al menos 5 caracteres."));
        messages.add(new Message("user.register.idNumber.too.long",
                "El número de identificación no puede superar 20 caracteres."));
        messages.add(new Message("user.register.idNumber.duplicate",
                "Ya existe un usuario con este número de identificación."));

        // --- firstName ---
        messages.add(new Message("user.register.firstName.required",
                "El campo 'Nombre' es obligatorio."));
        messages.add(new Message("user.register.firstName.too.short",
                "El nombre debe tener al menos 1 caracteres."));
        messages.add(new Message("user.register.firstName.tooLong",
                "El nombre no puede superar 60 caracteres."));
        messages.add(new Message("user.register.firstName.invalidFormat",
                "El primer nombre solo puede contener letras y espacios."));

        // --- secondName (opcional) ---
        messages.add(new Message("user.register.secondName.too.short",
                "El segundo nombre debe tener al menos 1 caracteres."));
        messages.add(new Message("user.register.secondName.too.long",
                "El segundo nombre no puede superar 60 caracteres."));
        messages.add(new Message("user.register.secondName.invalidFormat",
                "El segundo nombre solo puede contener letras y espacios."));

        // --- firstSurname ---
        messages.add(new Message("user.register.firstSurname.required",
                "El primer apellido es obligatorio."));
        messages.add(new Message("user.register.firstSurname.too.short",
                "El primer apellido debe tener al menos 1 caracteres."));
        messages.add(new Message("user.register.firstSurname.too.long",
                "El primer apellido no puede superar 60 caracteres."));
        messages.add(new Message("user.register.firstSurname.invalidFormat",
                "El primer apellido solo puede contener letras y espacios."));

        // --- secondSurname (opcional) ---
        messages.add(new Message("user.register.secondSurname.too.short",
                "El segundo apellido debe tener al menos 1 caracteres."));
        messages.add(new Message("user.register.secondSurname.too.long",
                "El segundo apellido no puede superar 60 caracteres."));
        messages.add(new Message("user.register.secondSurname.invalidFormat",
                "El segundo apellido solo puede contener letras y espacios."));

        // --- homeCity ---
        messages.add(new Message("user.register.homeCity.required",
                "Debes seleccionar una ciudad."));
        messages.add(new Message("user.register.homeCity.notFound",
                "La ciudad seleccionada no existe."));

        // --- email ---
        messages.add(new Message("user.register.email.required",
                "El correo es obligatorio."));
        messages.add(new Message("user.register.email.too.short",
                "El correo debe tener al menos 5 caracteres."));
        messages.add(new Message("user.register.email.too.long",
                "El correo no puede superar 120 caracteres."));
        messages.add(new Message("user.register.email.invalid",
                "El correo no tiene un formato válido."));

        // --- mobileNumber (opcional, o úsalo si alguna vez es obligatorio) ---
        messages.add(new Message("user.register.mobile.required",
                "El número de celular es obligatorio."));
        messages.add(new Message("user.register.mobile.too.short",
                "El número de celular debe tener al menos 7 dígitos."));
        messages.add(new Message("user.register.mobile.too.long",
                "El número de celular no puede superar 15 dígitos."));
        messages.add(new Message("user.register.mobile.invalid",
                "El número de celular debe contener únicamente dígitos (0–9), sin espacios ni símbolos."));
        messages.add(new Message("user.register.mobile.duplicate",
                "Ya existe un usuario registrado con este número de celular."));

        // --- Confirmación / Tokens ---
        messages.add(new Message("user.register.confirmation.token.failed",
                "No fue posible generar los tokens de confirmación."));

        // --- Notificaciones de confirmación ---
        messages.add(new Message("user.confirmation.email.subject",
                "Confirma tu cuenta en UCO Challenge"));
        messages.add(new Message("user.confirmation.email.body",
                "Hola {0}, utiliza el código {1} para confirmar tu registro antes de {2}."));
        messages.add(new Message("user.confirmation.mobile.body",
                "Tu código de verificación es {0}. Vence el {1}."));

        // --- Resultado (también en Éxito) ---
        messages.add(new Message("USUARIO_CREADO_OK",
                "Usuario creado exitosamente."));

        // ==== Compatibilidad: claves MAYÚSCULAS para formato inválido ====
        messages.add(new Message("FIRST_NAME_INVALID_FORMAT",
                "El primer nombre solo puede contener letras y espacios."));
        messages.add(new Message("FIRST_SURNAME_INVALID_FORMAT",
                "El primer apellido solo puede contener letras y espacios."));
        messages.add(new Message("SECOND_NAME_INVALID_FORMAT",
                "El segundo nombre solo puede contener letras y espacios."));
        messages.add(new Message("SECOND_SURNAME_INVALID_FORMAT",
                "El segundo apellido solo puede contener letras y espacios."));

        // =========================
        // ⚠️ Negocio
        // =========================
        messages.add(new Message("USUARIO_YA_EXISTE", "El usuario ya existe."));
        messages.add(new Message("EMAIL_TAKEN", "El correo {0} ya está registrado."));
        messages.add(new Message("USER_NOT_FOUND", "No encontramos un usuario con el correo {0}."));
        messages.add(new Message("USUARIO_NO_ENCONTRADO", "No existe un usuario con el identificador proporcionado."));
        messages.add(new Message("USUARIO_INACTIVO", "El usuario se encuentra inactivo."));
        messages.add(new Message("USUARIO_BLOQUEADO", "El usuario ha sido bloqueado por múltiples intentos fallidos."));
        messages.add(new Message("PASSWORD_INVALIDO", "La contraseña ingresada no es válida."));
        messages.add(new Message("PASSWORD_EXPIRADO", "La contraseña ha expirado. Por favor, restablécela."));
        messages.add(new Message("ROL_NO_AUTORIZADO", "El rol del usuario no tiene permisos para realizar esta acción."));
        messages.add(new Message("TOKEN_INVALIDO", "El token proporcionado no es válido o ha expirado."));
        messages.add(new Message("ACCION_NO_PERMITIDA", "La acción solicitada no está permitida."));
        messages.add(new Message("OPERACION_NO_PERMITIDA", "No tienes permisos para realizar esta operación."));
        messages.add(new Message("ENTIDAD_NO_ENCONTRADA", "La entidad solicitada no fue encontrada."));
        messages.add(new Message("DATO_DUPLICADO", "Ya existe un registro con los mismos datos."));
        messages.add(new Message("VALIDACION_FALLIDA", "Algunos datos no cumplen las reglas de validación."));
        messages.add(new Message("PARAMETRO_NO_ENCONTRADO", "El parámetro solicitado no fue encontrado."));

        return messages;
    }
}