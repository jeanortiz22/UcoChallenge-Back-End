package co.edu.uco.ucochallenge.user.listusers.application.interactor.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.ListUsersInteractor;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.UserResponseDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.ListUsersUseCase;

@Service
@Transactional(readOnly = true)
public class ListUsersInteractorImpl implements ListUsersInteractor {

    private final ListUsersUseCase useCase;

    public ListUsersInteractorImpl(final ListUsersUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public List<UserResponseDTO> execute(final Void input) {
        final var domains = useCase.execute(null);
        return domains.stream()
            .filter(Objects::nonNull)
            .map(domain -> new UserResponseDTO(
                    domain.getId(),
                    domain.getIdType(),
                    domain.getIdNumber(),
                    domain.getFirstName(),
                    domain.getSecondName(),
                    domain.getFirstSurname(),
                    domain.getSecondSurname(),
                    domain.getHomeCity(),
                    domain.getEmail(),
                    domain.getMobileNumber()))
            .toList();
    }
}