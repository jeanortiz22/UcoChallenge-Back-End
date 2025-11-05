package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.confirmation.application.service.ConfirmationNotificationService;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ConfirmationTokens;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserInputDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserResultDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.ConfirmationTokenService;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.DuplicateRegistrationNotifier;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.parameters.RegisterUserParameters;
import co.edu.uco.ucochallenge.user.registeruser.application.parameters.RegisterUserParametersProvider;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseImplTest {

    @Mock
    private RegisterUserGateway registerUserGateway;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private DuplicateRegistrationNotifier duplicateRegistrationNotifier;

    @Mock
    private ConfirmationNotificationService confirmationNotificationService;

    @Mock
    private RegisterUserParametersProvider parametersProvider;

    private RegisterUserUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterUserUseCaseImpl(
                registerUserGateway,
                confirmationTokenService,
                duplicateRegistrationNotifier,
                confirmationNotificationService,
                parametersProvider);
    }

    @Test
    void execute_whenInputDomainIsNull_shouldThrowApplicationException() {
        final var exception = assertThrows(UcoChallengeApplicationException.class, () -> useCase.execute(null));

        assertEquals(RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED, exception.getMessageCode());
    }

    @Test
    void execute_whenInputIsValid_shouldPersistValidatedAggregateAndReturnSuccess() {
        final var idType = UUID.randomUUID();
        final var homeCity = UUID.randomUUID();
        final var input = RegisterUserInputDomain.create(
                idType,
                "1234567890",
                "Camilo",
                "Andres",
                "Sanchez",
                "Lopez",
                homeCity,
                "USER@Test.com",
                "3001234567");

        final var parameters = new RegisterUserParameters(
                5, 30,
                2, 50,
                0, 50,
                2, 50,
                0, 50,
                5, 120, "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$",
                7, 15, "^[0-9]+$",
                "^[A-Za-z]+$",
                60, 15,
                3,
                true);

        final var tokens = new ConfirmationTokens(
                "email-token",
                LocalDateTime.now().plusMinutes(60),
                "sms-token",
                LocalDateTime.now().plusMinutes(15));

        when(parametersProvider.snapshot()).thenReturn(parameters);
        when(registerUserGateway.existsCity(homeCity)).thenReturn(true);
        when(registerUserGateway.existsById(any())).thenReturn(false);
        when(registerUserGateway.existsByIdentification(any(), anyString())).thenReturn(false);
        when(registerUserGateway.existsByEmail(anyString())).thenReturn(false);
        when(registerUserGateway.existsByMobileNumber(anyString())).thenReturn(false);
        when(confirmationTokenService.generateTokens(anyString(), anyString())).thenReturn(tokens);

        final RegisterUserResultDomain result = useCase.execute(input);

        assertNotNull(result);
        assertEquals(RegisterUserMessageCode.USER_REGISTERED_SUCCESSFULLY, result.messageCode());

        final ArgumentCaptor<RegisterUserDomain> domainCaptor = ArgumentCaptor.forClass(RegisterUserDomain.class);
        verify(registerUserGateway).save(domainCaptor.capture());

        final RegisterUserDomain persisted = domainCaptor.getValue();
        assertNotNull(persisted);
        assertEquals(result.id(), persisted.id());
        assertEquals("user@test.com", persisted.email());
        assertEquals("3001234567", persisted.mobileNumber());
        assertEquals(tokens.emailToken(), persisted.emailConfirmationToken());
        assertEquals(tokens.smsToken(), persisted.mobileConfirmationToken());

        verify(parametersProvider).snapshot();
        verify(confirmationTokenService).generateTokens("user@test.com", "3001234567");
    }
}