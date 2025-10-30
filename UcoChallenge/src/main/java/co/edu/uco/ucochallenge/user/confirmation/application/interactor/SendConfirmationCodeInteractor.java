package co.edu.uco.ucochallenge.user.confirmation.application.interactor;

import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.SendConfirmationCodeRequestDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.UserConfirmationResponseDTO;

public interface SendConfirmationCodeInteractor {

    UserConfirmationResponseDTO execute(SendConfirmationCodeRequestDTO request);
}