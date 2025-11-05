package co.edu.uco.ucochallenge.crosscuting.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UcoChallengeApplicationExceptionTest {

    @Test
    void createWithoutCauseShouldStoreMessageCodeAndParameters() {
        final UcoChallengeApplicationException exception =
                UcoChallengeApplicationException.create("CODE", " message ", "param");

        assertThat(exception.getMessage()).isEqualTo("message");
        assertThat(exception.getMessageCode()).isEqualTo("CODE");
        assertThat(exception.getParameters()).containsExactly("param");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void createWithCauseShouldIncludeCauseAndParameters() {
        final IllegalStateException cause = new IllegalStateException("boom");
        final UcoChallengeApplicationException exception =
                UcoChallengeApplicationException.create("CODE", "message", cause, 1, 2);

        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getParameters()).containsExactly(1, 2);
    }
}
