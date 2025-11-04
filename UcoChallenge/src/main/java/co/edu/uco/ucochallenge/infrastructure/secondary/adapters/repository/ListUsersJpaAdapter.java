package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository;

import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.ListUsersGateway;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

@Component
public class ListUsersJpaAdapter implements ListUsersGateway {

    private final SpringDataUserRepository userRepository;

    public ListUsersJpaAdapter(final SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserSummaryPageDomain findAll(final ListUsersQueryDomain query) {
        final var sanitizedQuery = ListUsersQueryDomain.from(query);
        final var pageRequest = PageRequest.of(sanitizedQuery.getPage(), sanitizedQuery.getSize());
        final var page = userRepository.findAll(pageRequest);

        final var users = page.getContent().stream()
                .filter(Objects::nonNull)
                .map(this::mapToDomain)
                .toList();
        
        return UserSummaryPageDomain.create(
                users,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
    

    private UserSummaryDomain mapToDomain(final UserEntity entity) {
        final var idType = entity.getIdType();
        final var homeCity = entity.getHomeCity();

        return UserSummaryDomain.create(
                entity.getId(),
                Objects.nonNull(idType) ? idType.getId() : null,
                entity.getIdNumber(),
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getFirstSurname(),
                entity.getSecondSurname(),
                Objects.nonNull(homeCity) ? homeCity.getId() : null,
                entity.getEmail(),
                entity.getMobileNumber(),
                entity.isEmailConfirmed(),
                entity.isMobileNumberConfirmed());
    }
}
