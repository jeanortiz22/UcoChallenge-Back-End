package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.fixtures.RegisterUserDomainFixture;

class RegisterUserEntityMapperTest {

    private final RegisterUserEntityMapper mapper = Mappers.getMapper(RegisterUserEntityMapper.class);

    @Test
    void toEntityShouldMapEveryField() {
        final RegisterUserDomain domain = RegisterUserDomainFixture.createDomain();

        final UserEntity entity = mapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(domain.id());
        assertThat(entity.getIdType()).isNotNull();
        assertThat(entity.getIdType().getId()).isEqualTo(domain.idType());
        assertThat(entity.getHomeCity()).isNotNull();
        assertThat(entity.getHomeCity().getId()).isEqualTo(domain.homeCity());
        assertThat(entity.getIdNumber()).isEqualTo(domain.idNumber());
        assertThat(entity.getFirstName()).isEqualTo(domain.firstName());
        assertThat(entity.getSecondName()).isEqualTo(domain.secondName());
        assertThat(entity.getFirstSurname()).isEqualTo(domain.firstSurname());
        assertThat(entity.getSecondSurname()).isEqualTo(domain.secondSurname());
        assertThat(entity.getEmail()).isEqualTo(domain.email());
        assertThat(entity.getMobileNumber()).isEqualTo(domain.mobileNumber());
        assertThat(entity.getEmailConfirmationToken()).isEqualTo(domain.emailConfirmationToken());
        assertThat(entity.getEmailConfirmationExpiresAt()).isEqualTo(domain.emailConfirmationExpiresAt());
        assertThat(entity.getMobileConfirmationToken()).isEqualTo(domain.mobileConfirmationToken());
        assertThat(entity.getMobileConfirmationExpiresAt()).isEqualTo(domain.mobileConfirmationExpiresAt());
        assertThat(entity.isEmailConfirmed()).isFalse();
        assertThat(entity.isMobileNumberConfirmed()).isFalse();
    }
}