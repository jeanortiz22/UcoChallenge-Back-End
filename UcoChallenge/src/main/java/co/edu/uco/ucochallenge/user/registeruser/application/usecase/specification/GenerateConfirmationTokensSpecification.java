package co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.ConfirmationTokenService;

public final class GenerateConfirmationTokensSpecification implements Specification<RegisterUserDomain> {

    private final ConfirmationTokenService confirmationTokenService;

    public GenerateConfirmationTokensSpecification(final ConfirmationTokenService confirmationTokenService) {
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public RegisterUserDomain apply(final RegisterUserDomain candidate) {
        if (ObjectHelper.isNull(candidate)) {
            throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                    "Register user domain is required");
        }

        final var tokens = confirmationTokenService.generateTokens(candidate);
        return candidate.withConfirmationTokens(tokens);
    }
}
