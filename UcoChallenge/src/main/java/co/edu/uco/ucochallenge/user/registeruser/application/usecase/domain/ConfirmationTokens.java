package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.time.LocalDateTime;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

public record ConfirmationTokens(
        String emailToken,
        LocalDateTime emailExpiresAt,
        String smsToken,
        LocalDateTime smsExpiresAt) {

    public ConfirmationTokens {
        emailToken = validateToken(emailToken, "emailToken");
        emailExpiresAt = validateDate(emailExpiresAt, "emailExpiresAt");
        smsToken = validateToken(smsToken, "smsToken");
        smsExpiresAt = validateDate(smsExpiresAt, "smsExpiresAt");
    }

    private static String validateToken(final String value, final String fieldName) {
        final var sanitized = TextHelper.getDefaultWithTrim(value);
        if (TextHelper.isEmpty(sanitized)) {
            throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.CONFIRMATION_TOKEN_GENERATION_FAILED,
                    "Confirmation token is required",
                    fieldName);
        }
        return sanitized;
    }

    private static LocalDateTime validateDate(final LocalDateTime value, final String fieldName) {
        if (ObjectHelper.isNull(value)) {
            throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.CONFIRMATION_TOKEN_GENERATION_FAILED,
                    "Confirmation token expiration is required",
                    fieldName);
        }
        return value;
    }
}