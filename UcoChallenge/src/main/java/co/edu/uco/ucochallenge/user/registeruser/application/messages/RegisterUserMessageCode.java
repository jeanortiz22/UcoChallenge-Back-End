package co.edu.uco.ucochallenge.user.registeruser.application.messages;

public final class RegisterUserMessageCode {

    // === Formato inválido (solo letras permitidas) ===
    public static final String FIRST_NAME_INVALID_FORMAT = "FIRST_NAME_INVALID_FORMAT";
    public static final String FIRST_SURNAME_INVALID_FORMAT = "FIRST_SURNAME_INVALID_FORMAT";
    public static final String SECOND_NAME_INVALID_FORMAT = "SECOND_NAME_INVALID_FORMAT";
    public static final String SECOND_SURNAME_INVALID_FORMAT = "SECOND_SURNAME_INVALID_FORMAT";


    private RegisterUserMessageCode() {}

    // ===== Input / Agregado =====
    public static final String INPUT_DATA_REQUIRED                     = "user.register.input.required";
    public static final String INPUT_DOMAIN_REQUIRED                   = "user.register.input.domain.required";
    public static final String USER_IDENTIFIER_REQUIRED                = "user.register.domain.identifier.required";
    public static final String USER_IDENTIFIER_GENERATION_FAILED       = "user.register.domain.identifier.generation.failed";

    // ===== idType =====
    public static final String ID_TYPE_REQUIRED                        = "user.register.idType.required";

    // ===== idNumber =====
    public static final String ID_NUMBER_REQUIRED                      = "user.register.idNumber.required";
    public static final String ID_NUMBER_TOO_SHORT                     = "user.register.idNumber.too.short";
    public static final String ID_NUMBER_TOO_LONG                      = "user.register.idNumber.too.long";
    public static final String IDENTIFICATION_ALREADY_EXISTS           = "user.register.idNumber.duplicate";

    // ===== firstName =====
    public static final String FIRST_NAME_REQUIRED                     = "user.register.firstName.required";
    public static final String FIRST_NAME_TOO_SHORT                    = "user.register.firstName.too.short";
    public static final String FIRST_NAME_TOO_LONG                     = "user.register.firstName.too.long";

    // ===== secondName (opcional) =====
    public static final String SECOND_NAME_TOO_SHORT                   = "user.register.secondName.too.short";
    public static final String SECOND_NAME_TOO_LONG                    = "user.register.secondName.too.long";

    // ===== firstSurname =====
    public static final String FIRST_SURNAME_REQUIRED                  = "user.register.firstSurname.required";
    public static final String FIRST_SURNAME_TOO_SHORT                 = "user.register.firstSurname.too.short";
    public static final String FIRST_SURNAME_TOO_LONG                  = "user.register.firstSurname.too.long";

    // ===== secondSurname (opcional) =====
    public static final String SECOND_SURNAME_TOO_SHORT                = "user.register.secondSurname.too.short";
    public static final String SECOND_SURNAME_TOO_LONG                 = "user.register.secondSurname.too.long";

    // ===== homeCity =====
    public static final String HOME_CITY_REQUIRED                      = "user.register.homeCity.required";
    public static final String HOME_CITY_NOT_FOUND                     = "user.register.homeCity.notFound";

    // ===== email =====
    public static final String EMAIL_REQUIRED                          = "user.register.email.required";
    public static final String EMAIL_TOO_SHORT                         = "user.register.email.too.short";
    public static final String EMAIL_TOO_LONG                          = "user.register.email.too.long";
    public static final String EMAIL_INVALID                           = "user.register.email.invalid";
    public static final String EMAIL_ALREADY_EXISTS                    = "user.register.email.duplicate";

    // ===== mobileNumber (opcional) =====
    public static final String MOBILE_NUMBER_REQUIRED                  = "user.register.mobile.required"; // si algún día lo haces obligatorio
    public static final String MOBILE_TOO_SHORT                        = "user.register.mobile.too.short";
    public static final String MOBILE_TOO_LONG                         = "user.register.mobile.too.long";
    public static final String MOBILE_INVALID                          = "user.register.mobile.invalid";
    public static final String MOBILE_NUMBER_ALREADY_EXISTS            = "user.register.mobile.duplicate";

    // ===== Confirmación =====
    public static final String CONFIRMATION_TOKEN_GENERATION_FAILED    = "user.register.confirmation.token.failed";

    // ===== Resultado =====
    public static final String USER_REGISTERED_SUCCESSFULLY            = "USUARIO_CREADO_OK";
}
