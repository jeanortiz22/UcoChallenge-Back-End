package co.edu.uco.ucochallenge.user.registeruser.application.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.RegisterUserInteractor;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegisterUserInteractorImpl implements RegisterUserInteractor {

    private final RegisterUserUseCase useCase;

    public RegisterUserInteractorImpl(final RegisterUserUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Void execute(final RegisterUserInputDTO dto) {
        if (ObjectHelper.isNull(dto)) {
            throw new IllegalArgumentException("Register user input data is required");
        }

        final var domain = RegisterUserDomain.create(
            dto.idType(),
            dto.idNumber(),
            dto.firstName(),
            dto.secondName(),
            dto.firstSurname(),
            dto.secondSurname(),
            dto.homeCity(),
            dto.email(),
            dto.mobileNumber());

        return useCase.execute(domain);
    }
}
