package co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public final class UniqueEmailSpecification implements Specification<RegisterUserDomain> {

    private final RegisterUserGateway registerUserGateway;

    public UniqueEmailSpecification(final RegisterUserGateway registerUserGateway) {
        this.registerUserGateway = registerUserGateway;
    }

    @Override
    public RegisterUserDomain apply(final RegisterUserDomain candidate) {
        if (ObjectHelper.isNull(candidate)) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                "Register user domain is required");
        }

        if (registerUserGateway.existsByEmail(candidate.email())) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.EMAIL_ALREADY_EXISTS,
                "A user with the same email already exists",
                candidate.email());
        }

        return candidate;
    }
}