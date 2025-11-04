package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event.DuplicateRegistrationEvent;

public interface DuplicateRegistrationNotifier {

    void notify(DuplicateRegistrationEvent event);
}
