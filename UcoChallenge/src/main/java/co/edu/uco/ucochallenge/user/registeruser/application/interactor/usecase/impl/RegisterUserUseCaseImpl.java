package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserInputDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserResultDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification.GenerateUniqueUserIdentifierSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification.UniqueEmailSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification.UniqueIdentificationSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification.UniqueMobileNumberSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.specification.ExistingHomeCitySpecification;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final RegisterUserGateway registerUserGateway;
    private final Specification<RegisterUserDomain> registerUserSpecification;


    public RegisterUserUseCaseImpl(final RegisterUserGateway registerUserGateway) {
        this.registerUserGateway = registerUserGateway;
        this.registerUserSpecification = Specification.<RegisterUserDomain>identity()
        		.and(new ExistingHomeCitySpecification(registerUserGateway))
                .and(new GenerateUniqueUserIdentifierSpecification(registerUserGateway))
                .and(new UniqueIdentificationSpecification(registerUserGateway))
                .and(new UniqueEmailSpecification(registerUserGateway))
                .and(new UniqueMobileNumberSpecification(registerUserGateway));
    }

    @Override
    public RegisterUserResultDomain execute(final RegisterUserInputDomain inputDomain) {
        if (ObjectHelper.isNull(inputDomain)) {
                throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                    "Register user input domain is required");
        }
        
        final var domain = RegisterUserDomain.fromInput(inputDomain);
        final var validatedDomain = registerUserSpecification.apply(domain);
        registerUserGateway.save(validatedDomain);
        return RegisterUserResultDomain.success(validatedDomain.id());
    }
}
