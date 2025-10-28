package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserInputDomain;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final RegisterUserGateway registerUserGateway;

    public RegisterUserUseCaseImpl(final RegisterUserGateway registerUserGateway) {
        this.registerUserGateway = registerUserGateway;
    }

    @Override
    public Void execute(final RegisterUserInputDomain inputDomain) {
        if (ObjectHelper.isNull(inputDomain)) {
                throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                    "Register user input domain is required");
        }
        
        final var domain = RegisterUserDomain.fromInput(inputDomain);
        registerUserGateway.save(domain);
        return Void.returnVoid();
    }
}
