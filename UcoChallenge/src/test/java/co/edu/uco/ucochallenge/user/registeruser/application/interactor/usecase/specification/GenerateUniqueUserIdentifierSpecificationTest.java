package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.fixtures.RegisterUserDomainFixture;

class GenerateUniqueUserIdentifierSpecificationTest {

    private final RegisterUserGateway gateway = mock(RegisterUserGateway.class);
    private final GenerateUniqueUserIdentifierSpecification specification =
            new GenerateUniqueUserIdentifierSpecification(gateway);

    @Test
    void applyShouldRejectNullCandidate() {
        assertThatThrownBy(() -> specification.apply(null))
                .isInstanceOf(UcoChallengeApplicationException.class)
                .hasMessageContaining(RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED);
    }

    @Test
    void applyShouldReturnCandidateWhenIdentifierIsUnique() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();

        doReturn(false).when(gateway).existsById(candidate.id());

        assertThat(specification.apply(candidate)).isSameAs(candidate);
    }

    @Test
    void applyShouldRetryUntilUniqueIdentifierIsFound() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();
        final AtomicInteger invocationCounter = new AtomicInteger();

        doAnswer(invocation -> invocationCounter.getAndIncrement() < 2)
                .when(gateway)
                .existsById(any());

        final RegisterUserDomain result = specification.apply(candidate);

        assertThat(invocationCounter.get()).isEqualTo(3);
        assertThat(result.id()).isNotEqualTo(candidate.id());
    }

    @Test
    void applyShouldFailWhenMaximumAttemptsExceeded() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();

        doAnswer(invocation -> true).when(gateway).existsById(any());

        assertThatThrownBy(() -> specification.apply(candidate))
                .isInstanceOf(UcoChallengeApplicationException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.USER_IDENTIFIER_GENERATION_FAILED);
    }
}