package co.edu.uco.ucochallenge.user.confirmation.application.usecase;

import co.edu.uco.ucochallenge.user.confirmation.application.domain.SendConfirmationCodeInputDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationResultDomain;

public interface SendConfirmationCodeUseCase {

    UserConfirmationResultDomain execute(SendConfirmationCodeInputDomain input);
}