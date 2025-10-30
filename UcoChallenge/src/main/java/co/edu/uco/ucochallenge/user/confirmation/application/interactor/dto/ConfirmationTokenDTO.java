package co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

public record ConfirmationTokenDTO(String token) {

    public String sanitizedToken() {
        return TextHelper.getDefaultWithTrim(token);
    }
}