package co.edu.uco.ucochallenge.user.confirmation.application.usecase;

import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationResultDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.VerifyConfirmationCodeInputDomain;

public interface VerifyConfirmationCodeUseCase {

    UserConfirmationResultDomain execute(VerifyConfirmationCodeInputDomain input);
}