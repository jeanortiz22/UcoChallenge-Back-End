package co.edu.uco.parametersservice.catalog;

import java.util.ArrayList;
import java.util.List;

final class ParameterCatalogDefaults {

    private ParameterCatalogDefaults() {}

    static List<Parameter> defaults() {
        List<Parameter> parameters = new ArrayList<>();

        // =========================
        // ✅ Generales / Operación
        // =========================
        parameters.add(new Parameter("FechaDefectoMaxima", "31/12/9999"));
        parameters.add(new Parameter("CorreoAdministrador", "admin@uco.edu.co"));
        parameters.add(new Parameter("NumeroMaximoReintentosEnvioCorreo", "8"));
        parameters.add(new Parameter("USER.REGISTER.DUPLICATE.ADMIN_EMAIL", "alertas.registro@uco.edu.co"));

        // =========================
        // 🧩 Registro de Usuario (coincide con RegisterUserParameterCode)
        // =========================

        // --- Documento (idNumber) ---
        // Rango sugerido por defecto: 5..20
        parameters.add(new Parameter("user.register.idNumber.min", "5"));
        parameters.add(new Parameter("user.register.idNumber.max", "20"));

        // --- Nombres y Apellidos ---
        // firstName: 1..50
        parameters.add(new Parameter("user.register.firstName.min", "1"));
        parameters.add(new Parameter("user.register.firstName.max", "50"));

        // secondName (opcional): 0..50 (0 permite omitir)
        parameters.add(new Parameter("user.register.secondName.min", "0"));
        parameters.add(new Parameter("user.register.secondName.max", "50"));

        // firstSurname: 2..50
        parameters.add(new Parameter("user.register.firstSurname.min", "2"));
        parameters.add(new Parameter("user.register.firstSurname.max", "50"));

        // secondSurname (opcional): 0..50
        parameters.add(new Parameter("user.register.secondSurname.min", "0"));
        parameters.add(new Parameter("user.register.secondSurname.max", "50"));

        // --- Email ---
        // Rango por defecto: 5..120
        parameters.add(new Parameter("user.register.email.min", "5"));
        parameters.add(new Parameter("user.register.email.max", "120"));
        // Regex de email (simple y segura)
        parameters.add(new Parameter("user.register.email.pattern", "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"));

        // --- Móvil ---
        // Rango por defecto: 10..15 (solo dígitos)
        parameters.add(new Parameter("user.register.mobile.min", "10"));
        parameters.add(new Parameter("user.register.mobile.max", "15"));
        parameters.add(new Parameter("user.register.mobile.pattern", "^[0-9]+$"));

        // --- Patrón para nombres ---
        // Letras + acentos + espacios + apóstrofe/guion (para nombres compuestos)
        parameters.add(new Parameter("user.register.name.pattern", "^[A-Za-zÁÉÍÓÚÑáéíóúñ\\s'-]+$"));

        // --- Políticas de seguridad ---
        // TTLs en minutos
        parameters.add(new Parameter("user.register.emailToken.ttlMin", "30"));
        parameters.add(new Parameter("user.register.smsToken.ttlMin", "10"));
        parameters.add(new Parameter("user.register.maxRetries", "5"));

        // --- Feature Flag ---
        parameters.add(new Parameter("user.register.feature.selfRegister.enabled", "true"));

        // =========================
        // 🔧 Compatibilidad / Extensiones
        // (por si quieres usar otras cosas luego)
        // =========================
        // Ejemplo de TTL específico móvil (si en algún momento lo diferencian)
        parameters.add(new Parameter("USER.REGISTER.MOBILE_CONFIRMATION_TTL_MINUTES", "15"));

        return parameters;
    }
}
