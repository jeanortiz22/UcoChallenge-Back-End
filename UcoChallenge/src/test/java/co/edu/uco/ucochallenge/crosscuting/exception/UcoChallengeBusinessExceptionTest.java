package co.edu.uco.ucochallenge.crosscuting.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UcoChallengeBusinessExceptionTest {

    @Test
    void createWithoutCauseShouldStoreMessageCodeAndParameters() {
        final UcoChallengeBusinessException exception =
                UcoChallengeBusinessException.create("CODE", "message", "param");

        assertThat(exception.getMessageCode()).isEqualTo("CODE");
        assertThat(exception.getMessage()).isEqualTo("message");
        assertThat(exception.getParameters()).containsExactly("param");
    }

    @Test
    void createWithCauseShouldIncludeCause() {
        final IllegalArgumentException cause = new IllegalArgumentException("error");
        final UcoChallengeBusinessException exception =
                UcoChallengeBusinessException.create("CODE", "message", cause, "param");

        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getParameters()).containsExactly("param");
    }
}