package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final RegisterUserGateway registerUserGateway;

    public RegisterUserUseCaseImpl(final RegisterUserGateway registerUserGateway) {
        this.registerUserGateway = registerUserGateway;
    }

    @Override
    public Void execute(final RegisterUserDomain domain) {
        if (ObjectHelper.isNull(domain)) {
        	throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.DOMAIN_DATA_REQUIRED,
                    "Register user domain is required");
        }

        registerUserGateway.save(domain);
        return Void.returnVoid();
    }
}
