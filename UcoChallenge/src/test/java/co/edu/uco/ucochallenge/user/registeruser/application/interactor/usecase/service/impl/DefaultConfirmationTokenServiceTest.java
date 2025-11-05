package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ConfirmationTokens;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

class DefaultConfirmationTokenServiceTest {

    @Test
    void generateTokensShouldUseCatalogValuesWhenAvailable() {
        final Map<String, String> values = new HashMap<>();
        values.put(DefaultConfirmationTokenService.EMAIL_TOKEN_TTL_CODE, "15");
        values.put(DefaultConfirmationTokenService.SMS_TOKEN_TTL_CODE, "5");

        final var service = new DefaultConfirmationTokenService(
                new InMemoryParameterCatalog(values),
                Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC),
                new DeterministicSecureRandom(0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5));

        final ConfirmationTokens tokens = service.generateTokens("user@example.com", "+573001234567");

        assertThat(tokens.emailToken()).isEqualTo("ABCDEF");
        assertThat(tokens.smsToken()).isEqualTo("123456");
        assertThat(tokens.emailExpiresAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 15));
        assertThat(tokens.smsExpiresAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 5));
    }

    @Test
    void generateTokensShouldFallbackToDefaultsWhenCatalogUnavailable() {
        final var service = new DefaultConfirmationTokenService(
                new FailingParameterCatalog(),
                Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC),
                new DeterministicSecureRandom(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

        final ConfirmationTokens tokens = service.generateTokens("user@example.com", "+573001234567");

        assertThat(tokens.emailExpiresAt()).isEqualTo(LocalDateTime.of(2024, 1, 2, 0, 0));
        assertThat(tokens.smsExpiresAt()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 10));
    }

    @Test
    void generateTokensShouldRejectEmptyContactInformation() {
        final var service = new DefaultConfirmationTokenService(
                new InMemoryParameterCatalog(Map.of()),
                Clock.systemUTC(),
                new DeterministicSecureRandom());

        assertThatThrownBy(() -> service.generateTokens(" ", " "))
                .isInstanceOf(UcoChallengeApplicationException.class)
                .extracting("messageCode")
                .isEqualTo(RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED);
    }

    private static final class InMemoryParameterCatalog implements ParameterCatalogPort {

        private final Map<String, String> values;

        private InMemoryParameterCatalog(final Map<String, String> values) {
            this.values = Map.copyOf(values);
        }

        @Override
        public String get(final String code, final Object... args) {
            return values.get(code);
        }

        @Override
        public String get(final String code, final Locale locale, final Object... args) {
            return values.get(code);
        }
    }

    private static final class FailingParameterCatalog implements ParameterCatalogPort {

        @Override
        public String get(final String code, final Object... args) {
            return DefaultConfirmationTokenService.CATALOG_UNAVAILABLE_PLACEHOLDER;
        }

        @Override
        public String get(final String code, final Locale locale, final Object... args) {
            throw new IllegalStateException("catalog not available");
        }
    }

    private static final class DeterministicSecureRandom extends SecureRandom {

        private static final long serialVersionUID = 1L;
        private final Deque<Integer> values;

        private DeterministicSecureRandom(final int... values) {
            this.values = new ArrayDeque<>();
            for (final int value : values) {
                this.values.add(value);
            }
        }

        @Override
        public int nextInt(final int bound) {
            if (values.isEmpty()) {
                return 0;
            }
            return values.removeFirst() % bound;
        }
    }
}