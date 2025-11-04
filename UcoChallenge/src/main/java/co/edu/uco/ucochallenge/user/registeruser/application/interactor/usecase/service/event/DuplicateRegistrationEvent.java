package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ExistingUserInformation;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;

public record DuplicateRegistrationEvent(
        DuplicateType type,
        RegisterUserDomain candidate,
        ExistingUserInformation existingUser) {

    public DuplicateRegistrationEvent {
        type = ObjectHelper.getDefault(type, DuplicateType.EMAIL);
        if (ObjectHelper.isNull(candidate)) {
            throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                    "Register user domain is required");
        }
    }
}
