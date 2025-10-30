package co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto;

import java.util.Locale;
import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.ConfirmationChannel;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;

public record SendConfirmationCodeRequestDTO(UUID userId, ConfirmationChannel channel) {

    public static SendConfirmationCodeRequestDTO create(final UUID userId, final String channelValue) {
        final var sanitizedId = UUIDHelper.getDefault(userId);
        final var sanitizedChannel = TextHelper.getDefaultWithTrim(channelValue);
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
        try {
            final var channel = ConfirmationChannel.valueOf(sanitizedChannel.toUpperCase(Locale.ROOT));
            return new SendConfirmationCodeRequestDTO(sanitizedId, channel);
        } catch (IllegalArgumentException ex) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Unsupported confirmation channel");
        }
    }
}