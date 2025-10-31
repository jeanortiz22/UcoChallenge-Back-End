package co.edu.uco.ucochallenge.user.listusers.application.interactor.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucochallenge.user.listusers.application.interactor.ListUsersInteractor;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.ListUsersRequestDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.PagedUsersResponseDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.UserResponseDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.ListUsersUseCase;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

@Service
@Transactional(readOnly = true)
public class ListUsersInteractorImpl implements ListUsersInteractor {

    private final ListUsersUseCase useCase;

    public ListUsersInteractorImpl(final ListUsersUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public PagedUsersResponseDTO execute(final ListUsersRequestDTO input) {
        final var normalizedInput = ListUsersRequestDTO.normalize(input);
        final var query = ListUsersQueryDomain.create(normalizedInput.page(), normalizedInput.size());
        final var pageDomain = useCase.execute(query);

        final var users = pageDomain.getUsers().stream()
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
                        domain.getMobileNumber(),
                        domain.isEmailConfirmed(),
                        domain.isMobileNumberConfirmed()))
                .toList();

        return PagedUsersResponseDTO.from(pageDomain, users);
    }
}