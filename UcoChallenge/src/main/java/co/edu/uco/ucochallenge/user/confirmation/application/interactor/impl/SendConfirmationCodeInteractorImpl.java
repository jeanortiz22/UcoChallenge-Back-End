package co.edu.uco.ucochallenge.user.confirmation.application.interactor.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.messages.MessageCatalogPort;
import co.edu.uco.ucochallenge.user.confirmation.application.domain.SendConfirmationCodeInputDomain;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.SendConfirmationCodeInteractor;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.SendConfirmationCodeRequestDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.interactor.dto.UserConfirmationResponseDTO;
import co.edu.uco.ucochallenge.user.confirmation.application.messages.UserConfirmationMessageCode;
import co.edu.uco.ucochallenge.user.confirmation.application.usecase.SendConfirmationCodeUseCase;

@Service
@Transactional
public class SendConfirmationCodeInteractorImpl implements SendConfirmationCodeInteractor {

    private final SendConfirmationCodeUseCase useCase;
    private final MessageCatalogPort messageCatalog;

    public SendConfirmationCodeInteractorImpl(
            final SendConfirmationCodeUseCase useCase,
            final MessageCatalogPort messageCatalog) {
        this.useCase = useCase;
        this.messageCatalog = messageCatalog;
    }

    @Override
    public UserConfirmationResponseDTO execute(final SendConfirmationCodeRequestDTO request) {
        if (ObjectHelper.isNull(request)) {
            throw UcoChallengeApplicationException.create(
                    UserConfirmationMessageCode.INPUT_DATA_REQUIRED,
                    "Send confirmation request is required");
        }

        final var inputDomain = new SendConfirmationCodeInputDomain(request.userId(), request.channel());
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
