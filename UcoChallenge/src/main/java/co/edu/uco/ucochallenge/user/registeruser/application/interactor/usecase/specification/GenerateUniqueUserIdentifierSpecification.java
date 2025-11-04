package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;

public final class GenerateUniqueUserIdentifierSpecification implements Specification<RegisterUserDomain> {

    private static final int MAX_ATTEMPTS = 5;

    private final RegisterUserGateway registerUserGateway;

    public GenerateUniqueUserIdentifierSpecification(final RegisterUserGateway registerUserGateway) {
        this.registerUserGateway = registerUserGateway;
    }

    @Override
    public RegisterUserDomain apply(final RegisterUserDomain candidate) {
        if (ObjectHelper.isNull(candidate)) {
            throw UcoChallengeApplicationException.create(
                RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                "Register user domain is required");
        }

        var attempts = 0;
        var current = candidate;

        while (registerUserGateway.existsById(current.id())) {
            if (attempts >= MAX_ATTEMPTS) {
                throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.USER_IDENTIFIER_GENERATION_FAILED,
                    "Unable to generate a unique identifier for the user aggregate");
            }

            current = current.withId(UUID.randomUUID());
            attempts++;
        }

        return current;
    }
}