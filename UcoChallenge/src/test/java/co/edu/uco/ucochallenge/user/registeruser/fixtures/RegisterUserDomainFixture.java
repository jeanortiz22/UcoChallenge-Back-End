package co.edu.uco.ucochallenge.user.registeruser.fixtures;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;

public final class RegisterUserDomainFixture {

    private RegisterUserDomainFixture() {
    }

    public static RegisterUserDomain createDomain() {
        return new RegisterUserDomain(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                "ID-123",
                "John",
                "Alexander",
                "Doe",
                "Smith",
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "john.doe@example.com",
                "+573001112233",
                "EMAIL-TOKEN",
                LocalDateTime.of(2024, 1, 1, 12, 0),
                "SMS-TOKEN",
                LocalDateTime.of(2024, 1, 1, 12, 5));
    }
}