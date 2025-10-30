package co.edu.uco.ucochallenge.user.confirmation.application.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public record UserConfirmationResultDomain(
        UUID userId,
        String messageCode,
        boolean emailConfirmed,
        boolean mobileConfirmed,
        boolean accountActivated) {

    public UserConfirmationResultDomain {
        userId = UUIDHelper.getDefault(userId);
        messageCode = TextHelper.getDefaultWithTrim(messageCode);
    }

    public static UserConfirmationResultDomain of(
            final UUID userId,
            final String messageCode,
            final boolean emailConfirmed,
            final boolean mobileConfirmed) {
        final var activated = emailConfirmed && mobileConfirmed;
        return new UserConfirmationResultDomain(userId, messageCode, emailConfirmed, mobileConfirmed, activated);
    }
}