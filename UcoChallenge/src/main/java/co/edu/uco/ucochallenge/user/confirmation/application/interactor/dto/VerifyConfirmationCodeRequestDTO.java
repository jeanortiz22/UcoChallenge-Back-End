package co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto;

import java.util.Locale;
import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.ConfirmationChannel;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;

public record VerifyConfirmationCodeRequestDTO(UUID userId, ConfirmationChannel channel, String token) {

    public static VerifyConfirmationCodeRequestDTO create(
            final UUID userId,
            final String channelValue,
            final String token) {
        final var sanitizedId = UUIDHelper.getDefault(userId);
        final var sanitizedChannel = TextHelper.getDefaultWithTrim(channelValue);
        final var sanitizedToken = TextHelper.getDefaultWithTrim(token);
        if (UUIDHelper.getDefault().equals(sanitizedId)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "User identifier is required");
        }
        if (TextHelper.isEmpty(sanitizedChannel)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Confirmation channel is required");
        }
        if (TextHelper.isEmpty(sanitizedToken)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.TOKEN_REQUIRED,
                    "Confirmation token is required");
        }
        try {
            final var channel = ConfirmationChannel.valueOf(sanitizedChannel.toUpperCase(Locale.ROOT));
            return new VerifyConfirmationCodeRequestDTO(sanitizedId, channel, sanitizedToken);
        } catch (IllegalArgumentException ex) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Unsupported confirmation channel");
        }
    }
}