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

class UniqueMobileNumberSpecificationTest {

    private final RegisterUserGateway gateway = mock(RegisterUserGateway.class);
    private final DuplicateRegistrationNotifier notifier = mock(DuplicateRegistrationNotifier.class);
    private final UniqueMobileNumberSpecification specification = new UniqueMobileNumberSpecification(gateway, notifier);

    @Test
    void applyShouldRejectNullCandidate() {
        assertThatThrownBy(() -> specification.apply(null))
                .isInstanceOf(UcoChallengeBusinessException.class)
                .hasMessageContaining(RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED);
    }

    @Test
    void applyShouldNotifyAndFailWhenMobileNumberAlreadyExists() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();
        final ExistingUserInformation existing = new ExistingUserInformation(
                candidate.id(),
                candidate.idType(),
                candidate.idNumber(),
                candidate.firstName(),
                candidate.firstSurname(),
                candidate.email(),
                candidate.mobileNumber());

        doReturn(true).when(gateway).existsByMobileNumber(candidate.mobileNumber());
        doReturn(Optional.of(existing)).when(gateway).findByMobileNumber(candidate.mobileNumber());

        assertThatThrownBy(() -> specification.apply(candidate))
                .isInstanceOf(UcoChallengeBusinessException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.MOBILE_NUMBER_ALREADY_EXISTS);

        verify(notifier).notify(new DuplicateRegistrationEvent(
                DuplicateType.MOBILE_NUMBER,
                candidate,
                existing));
    }

    @Test
    void applyShouldReturnCandidateWhenMobileNumberIsUnique() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();

        doReturn(false).when(gateway).existsByMobileNumber(candidate.mobileNumber());

        assertThat(specification.apply(candidate)).isSameAs(candidate);
    }
}