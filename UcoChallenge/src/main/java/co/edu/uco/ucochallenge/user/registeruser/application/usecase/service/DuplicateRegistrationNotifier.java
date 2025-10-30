package co.edu.uco.ucochallenge.user.registeruser.application.usecase.service;

import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.event.DuplicateRegistrationEvent;

public interface DuplicateRegistrationNotifier {

    void notify(DuplicateRegistrationEvent event);
}
