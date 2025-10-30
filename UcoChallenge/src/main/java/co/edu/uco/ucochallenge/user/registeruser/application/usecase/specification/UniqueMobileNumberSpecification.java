package co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification;


import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public final class UniqueMobileNumberSpecification implements Specification<RegisterUserDomain> {

    private final RegisterUserGateway registerUserGateway;

    public UniqueMobileNumberSpecification(final RegisterUserGateway registerUserGateway) {
        this.registerUserGateway = registerUserGateway;
    }

    @Override
    public RegisterUserDomain apply(final RegisterUserDomain candidate) {
        if (ObjectHelper.isNull(candidate)) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                "Register user domain is required");
        }

        if (registerUserGateway.existsByMobileNumber(candidate.mobileNumber())) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.MOBILE_NUMBER_ALREADY_EXISTS,
                "A user with the same mobile number already exists",
                candidate.mobileNumber());
        }

        return candidate;
    }
}