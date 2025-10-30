package co.edu.uco.ucochallenge.user.confirmation.application.usecase.impl;

import java.time.Clock;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.ConfirmationChannel;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationResultDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.VerifyConfirmationCodeInputDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;
import co.edu.uco.ucochallenge.user.confirmation.application.port.out.UserConfirmationGateway;
import co.edu.uco.ucochallenge.user.confirmation.application.usecase.VerifyConfirmationCodeUseCase;

@Service
public class VerifyConfirmationCodeUseCaseImpl implements VerifyConfirmationCodeUseCase {

    private static final Logger log = LoggerFactory.getLogger(VerifyConfirmationCodeUseCaseImpl.class);

    private final UserConfirmationGateway userConfirmationGateway;
    private final Clock clock;

    @Autowired
    public VerifyConfirmationCodeUseCaseImpl(final UserConfirmationGateway userConfirmationGateway) {
        this(userConfirmationGateway, Clock.systemUTC());
    }

    VerifyConfirmationCodeUseCaseImpl(
            final UserConfirmationGateway userConfirmationGateway,
            final Clock clock) {
        this.userConfirmationGateway = userConfirmationGateway;
        this.clock = ObjectHelper.getDefault(clock, Clock.systemUTC());
    }

    @Override
    public UserConfirmationResultDomain execute(final VerifyConfirmationCodeInputDomain input) {
        if (ObjectHelper.isNull(input)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Verify confirmation input is required");
        }

        final var user = userConfirmationGateway.findById(input.userId())
                .orElseThrow(() -> UcoChallengeApplicationException.create(
                        UserConfirmationMessageCode.USER_NOT_FOUND,
                        "User was not found"));

        if (input.channel() == ConfirmationChannel.EMAIL) {
            return verifyEmail(input, user);
        }
        return verifyMobile(input, user);
    }

    private UserConfirmationResultDomain verifyEmail(
            final VerifyConfirmationCodeInputDomain input,
            final UserConfirmationDomain user) {
        if (!user.hasEmail()) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.EMAIL_NOT_AVAILABLE,
                    "User does not have an email to confirm");
        }
        if (user.emailConfirmed()) {
            return UserConfirmationResultDomain.of(
                    user.id(),
                    UserConfirmationMessageCode.EMAIL_ALREADY_CONFIRMED,
                    user.emailConfirmed(),
                    user.mobileNumberConfirmed());
        }
        if (TextHelper.isEmpty(user.emailConfirmationToken())) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.TOKEN_INVALID,
                    "Email confirmation token is not available");
        }
        if (!user.emailConfirmationToken().equalsIgnoreCase(input.token())) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.TOKEN_INVALID,
                    "Email confirmation token is invalid");
        }
        final var now = LocalDateTime.now(clock);
        if (user.emailConfirmationExpiresAt().isBefore(now)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.TOKEN_EXPIRED,
                    "Email confirmation token expired");
        }

        final var updated = user.confirmEmail();
        userConfirmationGateway.save(updated);

        log.info("Correo confirmado para userId={}", user.id());
        return UserConfirmationResultDomain.of(
                updated.id(),
                UserConfirmationMessageCode.EMAIL_CONFIRMED,
                updated.emailConfirmed(),
                updated.mobileNumberConfirmed());
    }

    private UserConfirmationResultDomain verifyMobile(
            final VerifyConfirmationCodeInputDomain input,
            final UserConfirmationDomain user) {
        if (!user.hasMobileNumber()) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.MOBILE_NOT_AVAILABLE,
                    "User does not have a mobile number to confirm");
        }
        if (user.mobileNumberConfirmed()) {
            return UserConfirmationResultDomain.of(
                    user.id(),
                    UserConfirmationMessageCode.MOBILE_ALREADY_CONFIRMED,
                    user.emailConfirmed(),
                    user.mobileNumberConfirmed());
        }
        if (TextHelper.isEmpty(user.mobileConfirmationToken())) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.TOKEN_INVALID,
                    "Mobile confirmation token is not available");
        }
        if (!user.mobileConfirmationToken().equals(input.token())) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.TOKEN_INVALID,
                    "Mobile confirmation token is invalid");
        }
        final var now = LocalDateTime.now(clock);
        if (user.mobileConfirmationExpiresAt().isBefore(now)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.TOKEN_EXPIRED,
                    "Mobile confirmation token expired");
        }

        final var updated = user.confirmMobile();
        userConfirmationGateway.save(updated);

        log.info("Teléfono confirmado para userId={}", user.id());
        return UserConfirmationResultDomain.of(
                updated.id(),
                UserConfirmationMessageCode.MOBILE_CONFIRMED,
                updated.emailConfirmed(),
                updated.mobileNumberConfirmed());
    }
}