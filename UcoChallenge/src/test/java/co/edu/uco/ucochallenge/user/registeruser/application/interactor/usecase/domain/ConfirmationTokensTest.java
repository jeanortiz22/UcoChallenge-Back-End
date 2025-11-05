package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

class ConfirmationTokensTest {

    @Test
    void constructorShouldValidateTokens() {
        assertThatThrownBy(() -> new ConfirmationTokens(" ", LocalDateTime.now(), "123", LocalDateTime.now()))
                .isInstanceOf(UcoChallengeApplicationException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.CONFIRMATION_TOKEN_GENERATION_FAILED);
    }

    @Test
    void constructorShouldValidateExpirationDates() {
        assertThatThrownBy(() -> new ConfirmationTokens("EMAIL", null, "123", LocalDateTime.now()))
                .isInstanceOf(UcoChallengeApplicationException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.CONFIRMATION_TOKEN_GENERATION_FAILED);
    }

    @Test
    void shouldCreateImmutableInstanceWhenValidDataIsProvided() {
        final LocalDateTime now = LocalDateTime.now();
        final ConfirmationTokens tokens = new ConfirmationTokens("EMAIL", now.plusMinutes(5), "123456", now.plusMinutes(1));

        assertThat(tokens.emailToken()).isEqualTo("EMAIL");
        assertThat(tokens.smsToken()).isEqualTo("123456");
        assertThat(tokens.emailExpiresAt()).isEqualTo(now.plusMinutes(5));
        assertThat(tokens.smsExpiresAt()).isEqualTo(now.plusMinutes(1));
    }
}