package co.edu.uco.ucochallenge.secondary.adapters.repository;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.listusers.application.port.out.ListUsersGateway;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryDomain;

@Component
public class ListUsersJpaAdapter implements ListUsersGateway {

    private final SpringDataUserRepository userRepository;

    public ListUsersJpaAdapter(final SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserSummaryDomain> findAll() {
        return userRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(this::mapToDomain)
                .toList();
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
                entity.getMobileNumber());
    }
}