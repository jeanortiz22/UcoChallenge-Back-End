package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ConfirmationTokens;

public interface ConfirmationTokenService {

	ConfirmationTokens generateTokens(String email, String mobileNumber);
}

