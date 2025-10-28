package co.edu.uco.ucochallenge.user.registeruser.application.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.mapper.RegisterUserInputMapper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegisterUserInteractorImpl implements RegisterUserInteractor {

    private final RegisterUserUseCase useCase;
    private final RegisterUserInputMapper inputMapper;;

    public RegisterUserInteractorImpl(
    		final RegisterUserUseCase useCase,
    		final RegisterUserInputMapper inputMapper) {
        this.useCase = useCase;
        this.inputMapper = inputMapper;
    }

    @Override
    public Void execute(final RegisterUserInputDTO dto) {
        if (ObjectHelper.isNull(dto)) {
        		throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.INPUT_DATA_REQUIRED,
                    "Register user input data is required");
        }

        final var inputDomain = inputMapper.toDomain(dto);
        return useCase.execute(inputDomain);
    }
}
