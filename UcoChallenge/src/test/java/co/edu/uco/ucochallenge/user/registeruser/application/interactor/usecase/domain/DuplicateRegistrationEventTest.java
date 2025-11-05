package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event.DuplicateRegistrationEvent;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.event.DuplicateType;
import co.edu.uco.ucochallenge.user.registeruser.fixtures.RegisterUserDomainFixture;

class DuplicateRegistrationEventTest {

    @Test
    void constructorShouldRejectNullCandidate() {
        assertThatThrownBy(() -> new DuplicateRegistrationEvent(DuplicateType.EMAIL, null, null))
                .isInstanceOf(UcoChallengeApplicationException.class);
    }

    @Test
    void constructorShouldDefaultTypeWhenNull() {
        final var candidate = RegisterUserDomainFixture.createDomain();
        final var event = new DuplicateRegistrationEvent(null, candidate, null);

        assertThat(event.type()).isEqualTo(DuplicateType.EMAIL);
        assertThat(event.candidate()).isSameAs(candidate);
    }
}
