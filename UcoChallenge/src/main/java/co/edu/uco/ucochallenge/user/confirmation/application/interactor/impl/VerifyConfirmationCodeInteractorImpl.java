package co.edu.uco.ucochallenge.user.confirmation.application.interactor.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.messages.MessageCatalogPort;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.VerifyConfirmationCodeInputDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.VerifyConfirmationCodeInteractor;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.UserConfirmationResponseDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.VerifyConfirmationCodeRequestDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;
import co.edu.uco.ucochallenge.user.confirmation.application.usecase.VerifyConfirmationCodeUseCase;

@Service
@Transactional
public class VerifyConfirmationCodeInteractorImpl implements VerifyConfirmationCodeInteractor {

    private final VerifyConfirmationCodeUseCase useCase;
    private final MessageCatalogPort messageCatalog;

    public VerifyConfirmationCodeInteractorImpl(
            final VerifyConfirmationCodeUseCase useCase,
            final MessageCatalogPort messageCatalog) {
        this.useCase = useCase;
        this.messageCatalog = messageCatalog;
    }

    @Override
    public UserConfirmationResponseDTO execute(final VerifyConfirmationCodeRequestDTO request) {
        if (ObjectHelper.isNull(request)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Verify confirmation request is required");
        }

        final var inputDomain = new VerifyConfirmationCodeInputDomain(
                request.userId(),
                request.channel(),
                request.token());
        final var result = useCase.execute(inputDomain);
        final var message = messageCatalog.format(result.messageCode());

        return UserConfirmationResponseDTO.of(
                result.userId(),
                result.messageCode(),
                message,
                result.emailConfirmed(),
                result.mobileConfirmed(),
                result.accountActivated());
    }
}