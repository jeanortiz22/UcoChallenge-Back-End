package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ConfirmationTokens;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.ConfirmationTokenService;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.fixtures.RegisterUserDomainFixture;

class GenerateConfirmationTokensSpecificationTest {

    private final ConfirmationTokenService confirmationTokenService = mock(ConfirmationTokenService.class);
    private final GenerateConfirmationTokensSpecification specification =
            new GenerateConfirmationTokensSpecification(confirmationTokenService);

    @Test
    void applyShouldRejectNullCandidate() {
        assertThatThrownBy(() -> specification.apply(null))
                .isInstanceOf(UcoChallengeApplicationException.class)
                .hasMessageContaining(RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED);
    }

    @Test
    void applyShouldAttachGeneratedTokens() {
        final RegisterUserDomain candidate = RegisterUserDomainFixture.createDomain();
        final ConfirmationTokens tokens = new ConfirmationTokens(
                "NEW-EMAIL",
                LocalDateTime.now().plusMinutes(5),
                "123456",
                LocalDateTime.now().plusMinutes(1));

        doReturn(tokens).when(confirmationTokenService)
                .generateTokens(candidate.email(), candidate.mobileNumber());

        final RegisterUserDomain result = specification.apply(candidate);

        assertThat(result)
                .isNotSameAs(candidate)
                .extracting(RegisterUserDomain::emailConfirmationToken, RegisterUserDomain::mobileConfirmationToken)
                .containsExactly(tokens.emailToken(), tokens.smsToken());
    }
}