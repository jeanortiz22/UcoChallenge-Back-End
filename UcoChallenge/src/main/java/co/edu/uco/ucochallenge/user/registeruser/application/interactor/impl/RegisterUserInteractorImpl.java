package co.edu.uco.ucochallenge.user.registeruser.application.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.messages.MessageCatalogPort;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserResponseDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.mapper.RegisterUserInputMapper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegisterUserInteractorImpl implements RegisterUserInteractor {

    private final RegisterUserUseCase useCase;
    private final RegisterUserInputMapper inputMapper;
    private final MessageCatalogPort messageCatalog;

    public RegisterUserInteractorImpl(
    		final RegisterUserUseCase useCase,
            final RegisterUserInputMapper inputMapper,
            final MessageCatalogPort messageCatalog) {
        this.useCase = useCase;
        this.inputMapper = inputMapper;
        this.messageCatalog = messageCatalog;

    }

    @Override
    public RegisterUserResponseDTO execute(final RegisterUserInputDTO dto) {
        if (ObjectHelper.isNull(dto)) {
        	throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.INPUT_DATA_REQUIRED,
                    "Register user input data is required");
        }

        final var inputDomain = inputMapper.toDomain(dto);
        final var result = useCase.execute(inputDomain);
        final var message = messageCatalog.format(result.messageCode());
        return RegisterUserResponseDTO.of(result.id(), result.messageCode(), message);
    }
}
