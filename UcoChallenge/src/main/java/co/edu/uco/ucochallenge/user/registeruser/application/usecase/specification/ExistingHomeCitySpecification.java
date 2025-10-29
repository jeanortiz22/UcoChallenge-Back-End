package co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.crosscuting.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public final class ExistingHomeCitySpecification implements Specification<RegisterUserDomain> {

    private final RegisterUserGateway registerUserGateway;

    public ExistingHomeCitySpecification(final RegisterUserGateway registerUserGateway) {
        this.registerUserGateway = registerUserGateway;
    }

    @Override
    public RegisterUserDomain apply(final RegisterUserDomain candidate) {
        if (ObjectHelper.isNull(candidate)) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                "Register user domain is required");
        }

        final var homeCity = candidate.homeCity();
        if (UUIDHelper.getDefault().equals(homeCity)) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.HOME_CITY_REQUIRED,
                "Home city is required",
                "homeCity");
        }

        if (!registerUserGateway.existsCity(homeCity)) {
            throw UcoChallengeBusinessException.create(
                RegisterUserMessageCode.HOME_CITY_NOT_FOUND,
                "The provided home city does not exist",
                homeCity.toString());
        }

        return candidate;
    }
}