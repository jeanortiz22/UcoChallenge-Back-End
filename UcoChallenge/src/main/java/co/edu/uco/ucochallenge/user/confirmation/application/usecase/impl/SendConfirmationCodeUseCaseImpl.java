package co.edu.uco.ucochallenge.user.confirmation.application.usecase.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.ConfirmationChannel;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.SendConfirmationCodeInputDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationResultDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.UserConfirmationGateway;
import co.edu.uco.ucochallenge.user.confirmation.application.usecase.SendConfirmationCodeUseCase;
import co.edu.uco.ucochallenge.user.confirmation.application.service.ConfirmationNotificationService;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ConfirmationTokens;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.ConfirmationTokenService;

@Service
public class SendConfirmationCodeUseCaseImpl implements SendConfirmationCodeUseCase {

    private static final Logger log = LoggerFactory.getLogger(SendConfirmationCodeUseCaseImpl.class);

    private final UserConfirmationGateway userConfirmationGateway;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationNotificationService notificationService;

    public SendConfirmationCodeUseCaseImpl(
            final UserConfirmationGateway userConfirmationGateway,
            final ConfirmationTokenService confirmationTokenService,
            final ConfirmationNotificationService notificationService) {
        this.userConfirmationGateway = userConfirmationGateway;
        this.confirmationTokenService = confirmationTokenService;
        this.notificationService = notificationService;
    }

    @Override
    public UserConfirmationResultDomain execute(final SendConfirmationCodeInputDomain input) {
        if (ObjectHelper.isNull(input)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Send confirmation input is required");
        }

        final var user = userConfirmationGateway.findById(input.userId())
                .orElseThrow(() -> UcoChallengeApplicationException.create(
                        UserConfirmationMessageCode.USER_NOT_FOUND,
                        "User was not found"));

        if (input.channel() == ConfirmationChannel.EMAIL) {
            return handleEmailConfirmation(user);
        }
        return handleMobileConfirmation(user);
    }

    private UserConfirmationResultDomain handleEmailConfirmation(final UserConfirmationDomain user) {
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

        final ConfirmationTokens tokens = confirmationTokenService.generateTokens(user.email(), user.mobileNumber());
        final var updated = user.withEmailConfirmationToken(tokens.emailToken(), tokens.emailExpiresAt());
        userConfirmationGateway.save(updated);

        try {
            notificationService.sendEmailConfirmation(updated);
        } catch (Exception ex) {
            log.error("Error enviando confirmación de correo para userId={}: {}", user.id(), ex.getMessage(), ex);
        }

        return UserConfirmationResultDomain.of(
                updated.id(),
                UserConfirmationMessageCode.EMAIL_CONFIRMATION_SENT,
                updated.emailConfirmed(),
                updated.mobileNumberConfirmed());
    }

    private UserConfirmationResultDomain handleMobileConfirmation(final UserConfirmationDomain user) {
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

        final ConfirmationTokens tokens = confirmationTokenService.generateTokens(user.email(), user.mobileNumber());
        final var updated = user.withMobileConfirmationToken(tokens.smsToken(), tokens.smsExpiresAt());
        userConfirmationGateway.save(updated);

        try {
            notificationService.sendMobileConfirmation(updated);
        } catch (Exception ex) {
            log.error("Error enviando confirmación de móvil para userId={}: {}", user.id(), ex.getMessage(), ex);
        }

        return UserConfirmationResultDomain.of(
                updated.id(),
                UserConfirmationMessageCode.MOBILE_CONFIRMATION_SENT,
                updated.emailConfirmed(),
                updated.mobileNumberConfirmed());
    }
}