package co.edu.uco.ucochallenge.user.confirmation.application.messages;

public final class UserConfirmationMessageCode {

    private UserConfirmationMessageCode() {
    }

    public static final String INPUT_DATA_REQUIRED = "user.confirmation.input.data.required";
    public static final String USER_NOT_FOUND = "user.confirmation.user.not.found";
    public static final String EMAIL_NOT_AVAILABLE = "user.confirmation.email.not.available";
    public static final String MOBILE_NOT_AVAILABLE = "user.confirmation.mobile.not.available";
    public static final String EMAIL_CONFIRMATION_SENT = "user.confirmation.email.sent";
    public static final String MOBILE_CONFIRMATION_SENT = "user.confirmation.mobile.sent";
    public static final String EMAIL_ALREADY_CONFIRMED = "user.confirmation.email.already.confirmed";
    public static final String MOBILE_ALREADY_CONFIRMED = "user.confirmation.mobile.already.confirmed";
    public static final String EMAIL_CONFIRMED = "user.confirmation.email.confirmed";
    public static final String MOBILE_CONFIRMED = "user.confirmation.mobile.confirmed";
    public static final String TOKEN_REQUIRED = "user.confirmation.token.required";
    public static final String TOKEN_INVALID = "user.confirmation.token.invalid";
    public static final String TOKEN_EXPIRED = "user.confirmation.token.expired";
}