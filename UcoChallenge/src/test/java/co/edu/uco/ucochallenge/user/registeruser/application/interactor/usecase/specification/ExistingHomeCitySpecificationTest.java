package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.fixtures.RegisterUserDomainFixture;

class ExistingHomeCitySpecificationTest {

    private final RegisterUserGateway gateway = mock(RegisterUserGateway.class);
    private final ExistingHomeCitySpecification specification = new ExistingHomeCitySpecification(gateway);

    @Test
    void applyShouldRejectNullCandidate() {
        assertThatThrownBy(() -> specification.apply(null))
                .isInstanceOf(UcoChallengeBusinessException.class)
                .hasMessageContaining(RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED);
    }

    @Test
    void applyShouldFailWhenHomeCityIsDefault() {
        final RegisterUserDomain candidate = mock(RegisterUserDomain.class);
        when(candidate.homeCity()).thenReturn(UUIDHelper.getDefault());

        assertThatThrownBy(() -> specification.apply(candidate))
                .isInstanceOf(UcoChallengeBusinessException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.HOME_CITY_REQUIRED);
    }

    @Test
    void applyShouldFailWhenCityDoesNotExist() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();

        doReturn(false).when(gateway).existsCity(candidate.homeCity());

        assertThatThrownBy(() -> specification.apply(candidate))
                .isInstanceOf(UcoChallengeBusinessException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.HOME_CITY_NOT_FOUND);
    }

    @Test
    void applyShouldReturnCandidateWhenCityExists() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();

        doReturn(true).when(gateway).existsCity(candidate.homeCity());

        assertThat(specification.apply(candidate)).isSameAs(candidate);
    }
}