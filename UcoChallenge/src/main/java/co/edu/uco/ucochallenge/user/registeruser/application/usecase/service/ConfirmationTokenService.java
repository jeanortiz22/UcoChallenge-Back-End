package co.edu.uco.ucochallenge.user.registeruser.application.usecase.service;

import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.ConfirmationTokens;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public interface ConfirmationTokenService {

    ConfirmationTokens generateTokens(RegisterUserDomain domain);
}