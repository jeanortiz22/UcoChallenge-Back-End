package co.edu.uco.ucochallenge.user.registeruser.application.usecase.service;

import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.ConfirmationTokens;

public interface ConfirmationTokenService {

	ConfirmationTokens generateTokens(String email, String mobileNumber);
}