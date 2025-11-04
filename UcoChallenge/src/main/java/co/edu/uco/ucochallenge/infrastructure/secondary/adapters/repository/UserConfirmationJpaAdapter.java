package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.UserConfirmationGateway;

@Component
public class UserConfirmationJpaAdapter implements UserConfirmationGateway {

    private final SpringDataUserRepository userRepository;

    public UserConfirmationJpaAdapter(final SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserConfirmationDomain> findById(final UUID userId) {
        final var sanitizedId = UUIDHelper.getDefault(userId);
        if (UUIDHelper.getDefault().equals(sanitizedId)) {
            return Optional.empty();
        }
        return userRepository.findById(sanitizedId)
                .map(this::toDomain);
    }

    @Override
    public void save(final UserConfirmationDomain domain) {
        if (ObjectHelper.isNull(domain)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "User confirmation domain is required");
        }

        final var entity = userRepository.findById(domain.id())
                .orElseThrow(() -> UcoChallengeApplicationException.create(
                        UserConfirmationMessageCode.USER_NOT_FOUND,
                        "User was not found"));

        entity.updateEmailConfirmation(domain.emailConfirmationToken(),
                domain.emailConfirmationExpiresAt(),
                domain.emailConfirmed());
        entity.updateMobileConfirmation(domain.mobileConfirmationToken(),
                domain.mobileConfirmationExpiresAt(),
                domain.mobileNumberConfirmed());

        userRepository.save(entity);
    }

    private UserConfirmationDomain toDomain(final UserEntity entity) {
        final var idTypeEntity = entity.getIdType();
        return new UserConfirmationDomain(
                entity.getId(),
                idTypeEntity != null ? idTypeEntity.getId() : UUIDHelper.getDefault(),
                entity.getIdNumber(),
                entity.getFirstName(),
                entity.getFirstSurname(),
                entity.getEmail(),
                entity.getMobileNumber(),
                entity.getEmailConfirmationToken(),
                entity.getEmailConfirmationExpiresAt(),
                entity.isEmailConfirmed(),
                entity.getMobileConfirmationToken(),
                entity.getMobileConfirmationExpiresAt(),
                entity.isMobileNumberConfirmed());
    }
}