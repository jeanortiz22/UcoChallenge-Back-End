package co.edu.uco.messageservice.catalog;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class MessageCatalog {

    private static final Map<String, Message> MESSAGES = new ConcurrentHashMap<>();

    private MessageCatalog() { }

    static {
        // =========================
        // ✅ Éxito
        // =========================
        upsert(new Message("USUARIO_CREADO_OK", "Usuario creado exitosamente."));
        upsert(new Message("USUARIO_ACTUALIZADO_OK", "Usuario actualizado correctamente."));
        upsert(new Message("USUARIO_ELIMINADO_OK", "Usuario eliminado correctamente."));
        upsert(new Message("PARAMETRO_CREADO_OK", "Parámetro creado exitosamente."));
        upsert(new Message("PARAMETRO_ACTUALIZADO_OK", "Parámetro actualizado correctamente."));
        upsert(new Message("OPERACION_EXITOSA", "La operación se realizó correctamente."));
        upsert(new Message("DATOS_GUARDADOS_OK", "Los datos fueron guardados correctamente."));

        // Mantener compatibilidad con mensajes existentes
        upsert(new Message("user.register.email.duplicate", "El correo {0} ya está registrado."));

        // =========================
        // 🆕 Registro de Usuario (completo)
        // =========================

        // --- Input / Agregado ---
        upsert(new Message("user.register.input.required",
                "Debes proporcionar los datos de entrada."));
        upsert(new Message("user.register.input.domain.required",
                "El objeto de entrada para el registro de usuario es obligatorio."));
        upsert(new Message("user.register.domain.identifier.required",
                "El identificador del agregado de usuario es obligatorio."));
        upsert(new Message("user.register.domain.identifier.generation.failed",
                "No fue posible generar el identificador del usuario."));

        // --- idType ---
        upsert(new Message("user.register.idType.required",
                "Debes seleccionar un tipo de identificación."));

        // --- idNumber ---
        // (Ya existía: user.register.idNumber.required)
        upsert(new Message("user.register.idNumber.required",
                "El campo 'Número de identificación' es obligatorio."));
        upsert(new Message("user.register.idNumber.too.short",
                "El número de identificación debe tener al menos {0} caracteres."));
        upsert(new Message("user.register.idNumber.too.long",
                "El número de identificación no puede superar {0} caracteres."));
        upsert(new Message("user.register.idNumber.duplicate",
                "Ya existe un usuario con este número de identificación."));

        // --- firstName ---
        // (Ya existían: required, tooLong)
        upsert(new Message("user.register.firstName.required",
                "El campo 'Nombre' es obligatorio."));
        upsert(new Message("user.register.firstName.too.short",
                "El nombre debe tener al menos {0} caracteres."));
        upsert(new Message("user.register.firstName.tooLong",
                "El nombre no puede superar {0} caracteres."));
        upsert(new Message("user.register.firstName.invalidFormat",
                "El primer nombre solo puede contener letras y espacios."));

        // --- secondName (opcional) ---
        upsert(new Message("user.register.secondName.too.short",
                "El segundo nombre debe tener al menos {0} caracteres."));
        upsert(new Message("user.register.secondName.too.long",
                "El segundo nombre no puede superar {0} caracteres."));
        upsert(new Message("user.register.secondName.invalidFormat",
                "El segundo nombre solo puede contener letras y espacios."));

        // --- firstSurname ---
        upsert(new Message("user.register.firstSurname.required",
                "El primer apellido es obligatorio."));
        upsert(new Message("user.register.firstSurname.too.short",
                "El primer apellido debe tener al menos {0} caracteres."));
        upsert(new Message("user.register.firstSurname.too.long",
                "El primer apellido no puede superar {0} caracteres."));
        upsert(new Message("user.register.firstSurname.invalidFormat",
                "El primer apellido solo puede contener letras y espacios."));

        // --- secondSurname (opcional) ---
        upsert(new Message("user.register.secondSurname.too.short",
                "El segundo apellido debe tener al menos {0} caracteres."));
        upsert(new Message("user.register.secondSurname.too.long",
                "El segundo apellido no puede superar {0} caracteres."));
        upsert(new Message("user.register.secondSurname.invalidFormat",
                "El segundo apellido solo puede contener letras y espacios."));

        // --- homeCity ---
        // (Ya existía: user.register.homeCity.required)
        upsert(new Message("user.register.homeCity.required",
                "Debes seleccionar una ciudad."));
        upsert(new Message("user.register.homeCity.notFound",
                "La ciudad seleccionada no existe."));

        // --- email ---
        // (Ya existían: required, invalid)
        upsert(new Message("user.register.email.required",
                "El correo es obligatorio."));
        upsert(new Message("user.register.email.too.short",
                "El correo debe tener al menos {0} caracteres."));
        upsert(new Message("user.register.email.too.long",
                "El correo no puede superar {0} caracteres."));
        upsert(new Message("user.register.email.invalid",
                "El correo no tiene un formato válido."));
        // (Ya existía: user.register.email.duplicate)

        // --- mobileNumber (opcional, o úsalo si alguna vez es obligatorio) ---
        upsert(new Message("user.register.mobile.required",
                "El número de celular es obligatorio."));
        upsert(new Message("user.register.mobile.too.short",
                "El número de celular debe tener al menos {0} dígitos."));
        upsert(new Message("user.register.mobile.too.long",
                "El número de celular no puede superar {0} dígitos."));
        upsert(new Message("user.register.mobile.invalid",
                "El número de celular debe contener únicamente dígitos (0–9), sin espacios ni símbolos."));
        upsert(new Message("user.register.mobile.duplicate",
                "Ya existe un usuario registrado con este número de celular."));

        // --- Confirmación / Tokens ---
        upsert(new Message("user.register.confirmation.token.failed",
                "No fue posible generar los tokens de confirmación."));

        // --- Resultado (también en Éxito) ---
        upsert(new Message("USUARIO_CREADO_OK",
                "Usuario creado exitosamente."));

        // ==== Compatibilidad: claves MAYÚSCULAS para formato inválido ====
        upsert(new Message("FIRST_NAME_INVALID_FORMAT",
                "El primer nombre solo puede contener letras y espacios."));
        upsert(new Message("FIRST_SURNAME_INVALID_FORMAT",
                "El primer apellido solo puede contener letras y espacios."));
        upsert(new Message("SECOND_NAME_INVALID_FORMAT",
                "El segundo nombre solo puede contener letras y espacios."));
        upsert(new Message("SECOND_SURNAME_INVALID_FORMAT",
                "El segundo apellido solo puede contener letras y espacios."));

        // =========================
        // ⚠️ Negocio
        // =========================
        upsert(new Message("USUARIO_YA_EXISTE", "El usuario ya existe."));
        upsert(new Message("EMAIL_TAKEN", "El correo {0} ya está registrado."));
        upsert(new Message("USER_NOT_FOUND", "No encontramos un usuario con el correo {0}."));
        upsert(new Message("USUARIO_NO_ENCONTRADO", "No existe un usuario con el identificador proporcionado."));
        upsert(new Message("USUARIO_INACTIVO", "El usuario se encuentra inactivo."));
        upsert(new Message("USUARIO_BLOQUEADO", "El usuario ha sido bloqueado por múltiples intentos fallidos."));
        upsert(new Message("PASSWORD_INVALIDO", "La contraseña ingresada no es válida."));
        upsert(new Message("PASSWORD_EXPIRADO", "La contraseña ha expirado. Por favor, restablécela."));
        upsert(new Message("ROL_NO_AUTORIZADO", "El rol del usuario no tiene permisos para realizar esta acción."));
        upsert(new Message("TOKEN_INVALIDO", "El token proporcionado no es válido o ha expirado."));
        upsert(new Message("ACCION_NO_PERMITIDA", "La acción solicitada no está permitida."));
        upsert(new Message("OPERACION_NO_PERMITIDA", "No tienes permisos para realizar esta operación."));
        upsert(new Message("ENTIDAD_NO_ENCONTRADA", "La entidad solicitada no fue encontrada."));
        upsert(new Message("DATO_DUPLICADO", "Ya existe un registro con los mismos datos."));
        upsert(new Message("VALIDACION_FALLIDA", "Algunos datos no cumplen las reglas de validación."));
        upsert(new Message("PARAMETRO_NO_ENCONTRADO", "El parámetro solicitado no fue encontrado."));
        upsert(new Message("PARAMETRO_OBLIGATORIO", "El parámetro {0} es obligatorio."));
        upsert(new Message("VALOR_INVALIDO", "El valor proporcionado para {0} no es válido."));
        upsert(new Message("ESTADO_INVALIDO", "El estado actual de la entidad no permite esta operación."));
        upsert(new Message("FECHA_INVALIDA", "La fecha proporcionada no es válida."));
        upsert(new Message("RANGO_NO_VALIDO", "El rango de valores ingresado no es válido."));
        upsert(new Message("TIPO_DATO_INVALIDO", "El tipo de dato del campo {0} no es válido."));
        upsert(new Message("REQUERIMIENTO_NO_CUMPLIDO", "No se cumple con los requerimientos necesarios para esta operación."));

        // =========================
        // ⚙️ Aplicación
        // =========================
        upsert(new Message("APLICACION_NO_DISPONIBLE", "El servicio no se encuentra disponible en este momento."));
        upsert(new Message("ERROR_DESCONOCIDO", "Ha ocurrido un error desconocido."));
        upsert(new Message("ERROR_VALIDACION_ENTRADA", "Los datos ingresados no cumplen con las reglas esperadas."));
        upsert(new Message("FORMATO_NO_VALIDO", "El formato de la solicitud no es válido."));
        upsert(new Message("JSON_MAL_FORMADO", "El cuerpo de la petición tiene un formato JSON inválido."));
        upsert(new Message("CABECERA_REQUERIDA", "Falta una cabecera obligatoria en la solicitud."));
        upsert(new Message("RECURSO_NO_ENCONTRADO", "El recurso solicitado no fue encontrado."));
        upsert(new Message("RUTA_NO_VALIDA", "La ruta solicitada no es válida."));

        // =========================
        // 🗄️ Base de datos
        // =========================
        upsert(new Message("DB_CONNECTION_FAILED", "Error interno al conectarse a la base de datos."));
        upsert(new Message("DB_QUERY_FAILED", "Ocurrió un error al ejecutar la consulta en la base de datos."));
        upsert(new Message("DB_CONSTRAINT_VIOLATION", "Violación de restricción en la base de datos."));
        upsert(new Message("DB_DUPLICATE_KEY", "Ya existe un registro con la misma clave primaria o índice único."));
        upsert(new Message("DB_TIMEOUT", "La operación de base de datos excedió el tiempo máximo permitido."));
        upsert(new Message("DB_TRANSACTION_FAILED", "La transacción de base de datos no se completó correctamente."));

        // =========================
        // 🌐 Externos
        // =========================
        upsert(new Message("EXTERNAL_SERVICE_TIMEOUT", "El servicio externo no respondió a tiempo."));
        upsert(new Message("EXTERNAL_SERVICE_ERROR", "Ocurrió un error al comunicarse con un servicio externo."));
        upsert(new Message("INTEGRACION_FALLIDA", "Falló la integración con el sistema externo."));
        upsert(new Message("API_KEY_INVALIDA", "La clave API proporcionada no es válida."));
        upsert(new Message("API_NO_DISPONIBLE", "La API externa no está disponible actualmente."));
        upsert(new Message("PROVEEDOR_NO_RESPONDE", "El proveedor externo no responde. Intenta nuevamente más tarde."));

        // =========================
        // 🔒 Auth
        // =========================
        upsert(new Message("AUTH_CREDENCIALES_INVALIDAS", "Las credenciales proporcionadas son incorrectas."));
        upsert(new Message("AUTH_USUARIO_NO_EXISTE", "No existe un usuario con las credenciales proporcionadas."));
        upsert(new Message("AUTH_TOKEN_EXPIRADO", "El token de autenticación ha expirado."));
        upsert(new Message("AUTH_TOKEN_INVALIDO", "El token de autenticación no es válido."));
        upsert(new Message("AUTH_ACCESO_DENEGADO", "Acceso denegado. No tienes permisos para acceder a este recurso."));
        upsert(new Message("AUTH_SESION_FINALIZADA", "La sesión ha expirado. Inicia sesión nuevamente."));
        upsert(new Message("AUTH_SERVIDOR_NO_DISPONIBLE", "El servidor de autenticación no está disponible."));

        // =========================
        // 🧩 Técnicos
        // =========================
        upsert(new Message("FILE_NOT_FOUND", "El archivo solicitado no fue encontrado."));
        upsert(new Message("FILE_UPLOAD_FAILED", "Ocurrió un error al subir el archivo."));
        upsert(new Message("FILE_FORMAT_INVALID", "El formato del archivo no es válido."));
        upsert(new Message("SERVICE_UNAVAILABLE", "El servicio solicitado no está disponible temporalmente."));
        upsert(new Message("TIMEOUT_ERROR", "La solicitud excedió el tiempo máximo de espera."));
        upsert(new Message("NETWORK_ERROR", "Error de red al intentar comunicarse con otro servicio."));
        upsert(new Message("CONFIG_ERROR", "Error de configuración interna en el sistema."));
        upsert(new Message("UNEXPECTED_ERROR", "Ha ocurrido un error inesperado. Intenta nuevamente."));
        upsert(new Message("NULL_POINTER", "Error interno del sistema (referencia nula)."));
        upsert(new Message("ILLEGAL_STATE", "El sistema se encuentra en un estado inválido."));
        upsert(new Message("SERIALIZATION_ERROR", "Error al serializar o deserializar datos."));
        upsert(new Message("SECURITY_ERROR", "Error de seguridad detectado."));
        upsert(new Message("DEPENDENCY_ERROR", "Error al cargar una dependencia interna."));

        // =========================
        // 📬 Genéricos
        // =========================
        upsert(new Message("REQUEST_INVALIDA", "La solicitud no es válida."));
        upsert(new Message("RESPUESTA_VACIA", "La respuesta del servidor está vacía."));
        upsert(new Message("FORMATO_RESPUESTA_INVALIDO", "El formato de la respuesta no es válido."));
        upsert(new Message("OPERACION_NO_SOPORTADA", "La operación solicitada no está soportada."));
        upsert(new Message("PARAMETROS_INSUFICIENTES", "No se proporcionaron todos los parámetros requeridos."));
        upsert(new Message("DEBE_INICIAR_SESION", "Debe iniciar sesión para acceder a este recurso."));
        upsert(new Message("ACCESO_NO_AUTORIZADO", "No tienes autorización para realizar esta acción."));
        upsert(new Message("RECURSO_BLOQUEADO", "El recurso solicitado está bloqueado."));
        upsert(new Message("RECURSO_OCUPADO", "El recurso se encuentra en uso."));
        upsert(new Message("OPERACION_CANCELADA", "La operación fue cancelada por el usuario."));
    }

    // ---- API del catálogo ----
    public static Optional<Message> get(String key) {
        if (key == null || key.isBlank()) return Optional.empty();
        return Optional.ofNullable(MESSAGES.get(key));
    }

    public static List<Message> findBulk(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) return List.of();
        List<Message> out = new ArrayList<>();
        for (String k : keys) {
            var m = MESSAGES.get(k);
            if (m != null) out.add(m);
        }
        return out;
    }

    public static Map<String, Message> getAll() {
        return Collections.unmodifiableMap(MESSAGES); // evita mutaciones externas
    }

    public static void upsert(Message message) {
        if (message == null || message.getKey() == null || message.getKey().isBlank())
            throw new IllegalArgumentException("key/template obligatorios");
        MESSAGES.put(message.getKey(), message);
    }

    public static boolean remove(String key) {
        return MESSAGES.remove(key) != null;
    }

    public static boolean exists(String key) {
        return MESSAGES.containsKey(key);
    }
}
