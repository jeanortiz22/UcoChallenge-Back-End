package co.edu.uco.ucochallenge.user.confirmation.application.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;

public record SendConfirmationCodeInputDomain(UUID userId, ConfirmationChannel channel) {

    public SendConfirmationCodeInputDomain {
        if (UUIDHelper.getDefault().equals(UUIDHelper.getDefault(userId))) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "User identifier is required");
        }
        if (ObjectHelper.isNull(channel)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Confirmation channel is required");
        }
    }
}