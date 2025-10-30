package co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.DuplicateRegistrationNotifier;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.event.DuplicateRegistrationEvent;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.event.DuplicateType;

public final class UniqueIdentificationSpecification implements Specification<RegisterUserDomain> {

    private final RegisterUserGateway registerUserGateway;
    private final DuplicateRegistrationNotifier duplicateRegistrationNotifier;

    public UniqueIdentificationSpecification(
            final RegisterUserGateway registerUserGateway,
            final DuplicateRegistrationNotifier duplicateRegistrationNotifier) {
        this.registerUserGateway = registerUserGateway;
        this.duplicateRegistrationNotifier = duplicateRegistrationNotifier;
    }

    @Override
    public RegisterUserDomain apply(final RegisterUserDomain candidate) {
        if (ObjectHelper.isNull(candidate)) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                "Register user domain is required");
        }

        if (registerUserGateway.existsByIdentification(candidate.idType(), candidate.idNumber())) {
        	final var existing = registerUserGateway
                    .findByIdentification(candidate.idType(), candidate.idNumber())
                    .orElse(null);
            duplicateRegistrationNotifier.notify(new DuplicateRegistrationEvent(
                    DuplicateType.IDENTIFICATION,
                    candidate,
                    existing));
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.IDENTIFICATION_ALREADY_EXISTS,
                "A user with the same identification type and number already exists",
                candidate.idType(),
                candidate.idNumber());
        }

        return candidate;
    }
}