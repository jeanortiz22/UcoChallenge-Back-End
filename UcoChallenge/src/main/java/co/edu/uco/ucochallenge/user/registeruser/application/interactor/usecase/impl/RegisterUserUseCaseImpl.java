package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification.Specification;
import co.edu.uco.ucochallenge.user.confirmation.application.service.ConfirmationNotificationService;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.RegisterUserUseCase;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserInputDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserResultDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.ConfirmationTokenService;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.service.DuplicateRegistrationNotifier;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification.GenerateUniqueUserIdentifierSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification.UniqueEmailSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification.UniqueIdentificationSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification.UniqueMobileNumberSpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification.ExistingHomeCitySpecification;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.specification.GenerateConfirmationTokensSpecification;

// 🔽 NUEVO: provider de parámetros
import co.edu.uco.ucochallenge.user.registeruser.application.parameters.RegisterUserParameters;
import co.edu.uco.ucochallenge.user.registeruser.application.parameters.RegisterUserParametersProvider;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

	private final RegisterUserGateway registerUserGateway;
	private final Specification<RegisterUserDomain> registerUserSpecification;
	private final RegisterUserParametersProvider parametersProvider; // 🔽 NUEVO

	public RegisterUserUseCaseImpl(
			final RegisterUserGateway registerUserGateway,
			final ConfirmationTokenService confirmationTokenService,
			final DuplicateRegistrationNotifier duplicateRegistrationNotifier,
			final ConfirmationNotificationService confirmationNotificationService,
			final RegisterUserParametersProvider parametersProvider // 🔽 NUEVO
	) {
		this.registerUserGateway = registerUserGateway;
		this.parametersProvider = parametersProvider; // 🔽 NUEVO

		this.registerUserSpecification = Specification.<RegisterUserDomain>identity()
				.and(new ExistingHomeCitySpecification(registerUserGateway))
				.and(new GenerateUniqueUserIdentifierSpecification(registerUserGateway))
				.and(new UniqueIdentificationSpecification(registerUserGateway, duplicateRegistrationNotifier))
				.and(new UniqueEmailSpecification(registerUserGateway, duplicateRegistrationNotifier))
				.and(new UniqueMobileNumberSpecification(registerUserGateway, duplicateRegistrationNotifier))
				.and(new GenerateConfirmationTokensSpecification(confirmationTokenService));
	}

	@Override
	public RegisterUserResultDomain execute(final RegisterUserInputDomain inputDomain) {
		if (ObjectHelper.isNull(inputDomain)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
					"Register user input domain is required");
		}

		// 🔽 NUEVO: snapshot tipado desde el catálogo de parámetros
		final var params = parametersProvider.snapshot();

		// 🔽 CAMBIO: construir el agregado con parámetros dinámicos
		final var domain = RegisterUserDomain.fromInput(inputDomain, params);

		final var validatedDomain = registerUserSpecification.apply(domain);
		registerUserGateway.save(validatedDomain);

		return RegisterUserResultDomain.success(validatedDomain.id());
	}
}
