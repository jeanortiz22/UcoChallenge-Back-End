package co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto;

import java.util.UUID;

public record UserConfirmationResponseDTO(
        UUID userId,
        String messageCode,
        String message,
        boolean emailConfirmed,
        boolean mobileConfirmed,
        boolean accountActivated) {

    public static UserConfirmationResponseDTO of(
            final UUID userId,
            final String messageCode,
            final String message,
            final boolean emailConfirmed,
            final boolean mobileConfirmed,
            final boolean accountActivated) {
        return new UserConfirmationResponseDTO(
                userId,
                messageCode,
                message,
                emailConfirmed,
                mobileConfirmed,
                accountActivated);
    }
}