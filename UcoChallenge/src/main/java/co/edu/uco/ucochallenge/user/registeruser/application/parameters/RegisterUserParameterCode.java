package co.edu.uco.ucochallenge.user.registeruser.application.parameters;

/**
 * Catálogo de códigos de PARÁMETROS para el caso de registro de usuario.
 * Solo contiene CLAVES (keys) que se usarán para consultarlas en el catálogo.
 *
 * No contiene valores — los valores vienen desde:
 * - ParameterCatalog (en memoria / Redis / DB)
 * - o valores por defecto en RegisterUserParameterDefault
 */
public final class RegisterUserParameterCode {



    private RegisterUserParameterCode() {}

    // ===== Documento (idNumber) =====
    public static final String ID_NUMBER_MIN                     = "user.register.idNumber.min";
    public static final String ID_NUMBER_MAX                     = "user.register.idNumber.max";

    // ===== Nombres y Apellidos =====
    public static final String FIRST_NAME_MIN                    = "user.register.firstName.min";
    public static final String FIRST_NAME_MAX                    = "user.register.firstName.max";

    public static final String SECOND_NAME_MIN                   = "user.register.secondName.min";
    public static final String SECOND_NAME_MAX                   = "user.register.secondName.max";

    public static final String FIRST_SURNAME_MIN                 = "user.register.firstSurname.min";
    public static final String FIRST_SURNAME_MAX                 = "user.register.firstSurname.max";

    public static final String SECOND_SURNAME_MIN                = "user.register.secondSurname.min";
    public static final String SECOND_SURNAME_MAX                = "user.register.secondSurname.max";

    // ===== Email =====
    public static final String EMAIL_MIN                         = "user.register.email.min";
    public static final String EMAIL_MAX                         = "user.register.email.max";
    public static final String EMAIL_PATTERN                     = "user.register.email.pattern";

    // ===== Móvil =====
    public static final String MOBILE_MIN                        = "user.register.mobile.min";
    public static final String MOBILE_MAX                        = "user.register.mobile.max";
    public static final String MOBILE_PATTERN                    = "user.register.mobile.pattern";

    // ===== Patrón para nombres =====
    public static final String NAME_PATTERN                      = "user.register.name.pattern";

    // ===== Políticas de seguridad =====
    public static final String EMAIL_TOKEN_TTL_MIN               = "user.register.emailToken.ttlMin";
    public static final String SMS_TOKEN_TTL_MIN                 = "user.register.smsToken.ttlMin";
    public static final String MAX_RETRIES                       = "user.register.maxRetries";

    // ===== Feature Flag (Activar/Desactivar auto-registro) =====
    public static final String FEATURE_SELF_REGISTER_ENABLED     = "user.register.feature.selfRegister.enabled";
}
