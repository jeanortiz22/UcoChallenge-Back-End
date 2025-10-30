package co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.impl;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.ConfirmationTokens;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.service.ConfirmationTokenService;

@Service
public class DefaultConfirmationTokenService implements ConfirmationTokenService {

    private static final Logger log = LoggerFactory.getLogger(DefaultConfirmationTokenService.class);

    static final String EMAIL_TOKEN_TTL_CODE = "USER.REGISTER.EMAIL_TOKEN_TTL_MIN";
    static final String SMS_TOKEN_TTL_CODE = "USER.REGISTER.SMS_TOKEN_TTL_MIN";
    private static final int DEFAULT_EMAIL_TOKEN_TTL = 1_440;
    private static final int DEFAULT_SMS_TOKEN_TTL = 10;
    private static final int EMAIL_TOKEN_LENGTH = 32;
    private static final int SMS_TOKEN_LENGTH = 6;
    private static final String EMAIL_TOKEN_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String SMS_TOKEN_ALPHABET = "0123456789";

    private final ParameterCatalogPort parameterCatalog;
    private final Clock clock;
    private final SecureRandom secureRandom;

    @Autowired
    public DefaultConfirmationTokenService(final ParameterCatalogPort parameterCatalog) {
        this(parameterCatalog, Clock.systemUTC(), new SecureRandom());
    }

    DefaultConfirmationTokenService(
            final ParameterCatalogPort parameterCatalog,
            final Clock clock,
            final SecureRandom secureRandom) {
        this.parameterCatalog = Objects.requireNonNull(parameterCatalog, "Parameter catalog port is required");
        this.clock = ObjectHelper.getDefault(clock, Clock.systemUTC());
        this.secureRandom = ObjectHelper.getDefault(secureRandom, new SecureRandom());
    }

    @Override
    public ConfirmationTokens generateTokens(final RegisterUserDomain domain) {
        if (ObjectHelper.isNull(domain)) {
            throw UcoChallengeApplicationException.create(
                    RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
                    "Register user domain is required");
        }

        final var now = LocalDateTime.now(clock);
        final var emailToken = generateToken(EMAIL_TOKEN_ALPHABET, EMAIL_TOKEN_LENGTH);
        final var smsToken = generateToken(SMS_TOKEN_ALPHABET, SMS_TOKEN_LENGTH);
        final var emailExpiresAt = now.plusMinutes(readTtlMinutes(EMAIL_TOKEN_TTL_CODE, DEFAULT_EMAIL_TOKEN_TTL));
        final var smsExpiresAt = now.plusMinutes(readTtlMinutes(SMS_TOKEN_TTL_CODE, DEFAULT_SMS_TOKEN_TTL));

        return new ConfirmationTokens(emailToken, emailExpiresAt, smsToken, smsExpiresAt);
    }

    private int readTtlMinutes(final String parameterCode, final int defaultValue) {
        try {
            final var value = parameterCatalog.get(parameterCode, Locale.getDefault());
            final var sanitized = TextHelper.getDefaultWithTrim(value);
            if (TextHelper.isEmpty(sanitized)) {
                return defaultValue;
            }
            final var parsed = Integer.parseInt(sanitized);
            if (parsed <= 0) {
                return defaultValue;
            }
            return parsed;
        } catch (Exception ex) {
            log.warn("No fue posible obtener TTL para {}: {}", parameterCode, ex.getMessage());
            return defaultValue;
        }
    }

    private String generateToken(final String alphabet, final int length) {
        final var builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            final var index = secureRandom.nextInt(alphabet.length());
            builder.append(alphabet.charAt(index));
        }
        return builder.toString();
    }
}