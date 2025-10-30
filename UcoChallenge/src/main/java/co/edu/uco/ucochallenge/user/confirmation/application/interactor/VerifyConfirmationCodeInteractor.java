package co.edu.uco.ucochallenge.user.confirmation.application.interactor;

import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.UserConfirmationResponseDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.VerifyConfirmationCodeRequestDTO;

public interface VerifyConfirmationCodeInteractor {

    UserConfirmationResponseDTO execute(VerifyConfirmationCodeRequestDTO request);
}