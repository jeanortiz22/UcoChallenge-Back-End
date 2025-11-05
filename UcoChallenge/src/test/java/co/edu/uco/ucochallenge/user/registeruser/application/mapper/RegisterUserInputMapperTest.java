package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserInputDomain;

class RegisterUserInputMapperTest {

    private final RegisterUserInputMapper mapper = new RegisterUserInputMapper() { };

    @Test
    void toDomainShouldReturnNullWhenDtoIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toDomainShouldMapAllFields() {
        final RegisterUserInputDTO dto = new RegisterUserInputDTO(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "123", "John", "A", "Doe", "S", UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "john.doe@example.com", "+573001234567");

        final RegisterUserInputDomain domain = mapper.toDomain(dto);

        assertThat(domain).isNotNull();
        assertThat(domain.idType()).isEqualTo(dto.idType());
        assertThat(domain.idNumber()).isEqualTo(dto.idNumber());
        assertThat(domain.firstName()).isEqualTo(dto.firstName());
        assertThat(domain.secondName()).isEqualTo(dto.secondName());
        assertThat(domain.firstSurname()).isEqualTo(dto.firstSurname());
        assertThat(domain.secondSurname()).isEqualTo(dto.secondSurname());
        assertThat(domain.homeCity()).isEqualTo(dto.homeCity());
        assertThat(domain.email()).isEqualTo(dto.email());
        assertThat(domain.mobileNumber()).isEqualTo(dto.mobileNumber());
    }
}