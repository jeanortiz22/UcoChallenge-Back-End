package co.edu.uco.ucochallenge.user.confirmation.application.service;

import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationDomain;

public interface ConfirmationNotificationService {

    void sendEmailConfirmation(UserConfirmationDomain user);

    void sendMobileConfirmation(UserConfirmationDomain user);
}
