package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ExistingUserInformation;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.DuplicateRegistrationNotifier;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event.DuplicateRegistrationEvent;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event.DuplicateType;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.fixtures.RegisterUserDomainFixture;

class UniqueEmailSpecificationTest {

    private final RegisterUserGateway gateway = mock(RegisterUserGateway.class);
    private final DuplicateRegistrationNotifier notifier = mock(DuplicateRegistrationNotifier.class);
    private final UniqueEmailSpecification specification = new UniqueEmailSpecification(gateway, notifier);

    @Test
    void applyShouldRejectNullCandidate() {
        assertThatThrownBy(() -> specification.apply(null))
                .isInstanceOf(UcoChallengeBusinessException.class)
                .hasMessageContaining(RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED);
    }

    @Test
    void applyShouldNotifyAndFailWhenEmailAlreadyExists() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();
        final ExistingUserInformation existing = new ExistingUserInformation(
                candidate.id(),
                candidate.idType(),
                candidate.idNumber(),
                candidate.firstName(),
                candidate.firstSurname(),
                candidate.email(),
                candidate.mobileNumber());

        doReturn(true).when(gateway).existsByEmail(candidate.email());
        doReturn(Optional.of(existing)).when(gateway).findByEmail(candidate.email());

        assertThatThrownBy(() -> specification.apply(candidate))
                .isInstanceOf(UcoChallengeBusinessException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.EMAIL_ALREADY_EXISTS);

        verify(notifier).notify(new DuplicateRegistrationEvent(
                DuplicateType.EMAIL,
                candidate,
                existing));
    }

    @Test
    void applyShouldReturnCandidateWhenEmailIsUnique() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();

        doReturn(false).when(gateway).existsByEmail(candidate.email());

        assertThat(specification.apply(candidate)).isSameAs(candidate);
    }
}