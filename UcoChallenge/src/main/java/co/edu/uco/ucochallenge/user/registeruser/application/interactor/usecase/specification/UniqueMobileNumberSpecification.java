package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification;


import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.DuplicateRegistrationNotifier;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event.DuplicateRegistrationEvent;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event.DuplicateType;

public final class UniqueMobileNumberSpecification implements Specification<RegisterUserDomain> {

    private final RegisterUserGateway registerUserGateway;
    private final DuplicateRegistrationNotifier duplicateRegistrationNotifier;

    public UniqueMobileNumberSpecification(
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

        if (registerUserGateway.existsByMobileNumber(candidate.mobileNumber())) {
        	final var existing = registerUserGateway
                    .findByMobileNumber(candidate.mobileNumber())
                    .orElse(null);
            duplicateRegistrationNotifier.notify(new DuplicateRegistrationEvent(
                    DuplicateType.MOBILE_NUMBER,
                    candidate,
                    existing));
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.MOBILE_NUMBER_ALREADY_EXISTS,
                "A user with the same mobile number already exists",
                candidate.mobileNumber());
        }

        return candidate;
    }
}