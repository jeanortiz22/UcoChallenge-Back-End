package co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

public record RegisterUserResponseDTO(
        UUID userId,
        String messageCode,
        String message) {

    public RegisterUserResponseDTO {
        userId = Objects.requireNonNull(userId, "userId must not be null");
        messageCode = TextHelper.getDefaultWithTrim(messageCode);
        message = TextHelper.getDefaultWithTrim(message);
    }

    public static RegisterUserResponseDTO of(
            final UUID userId,
            final String messageCode,
            final String message) {
        return new RegisterUserResponseDTO(userId, messageCode, message);
    }
}
