package co.edu.uco.ucochallenge.user.registeruser.application.usecase.service;

import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public interface ConfirmationNotificationService {

    void notify(RegisterUserDomain user);
}