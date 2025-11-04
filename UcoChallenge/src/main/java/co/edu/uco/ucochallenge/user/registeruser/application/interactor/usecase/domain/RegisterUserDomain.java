package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;
import co.edu.uco.ucochallenge.user.registeruser.application.parameters.RegisterUserParameters;

public record RegisterUserDomain(
		UUID id,
		UUID idType,
		String idNumber,
		String firstName,
		String secondName,
		String firstSurname,
		String secondSurname,
		UUID homeCity,
		String email,
		String mobileNumber,
		String emailConfirmationToken,
		LocalDateTime emailConfirmationExpiresAt,
		String mobileConfirmationToken,
		LocalDateTime mobileConfirmationExpiresAt) {

	// === Constructor canónico: solo normaliza y valida invariantes mínimas (sin rangos)
	public RegisterUserDomain {
		id = validateIdentifier(
				id,
				RegisterUserMessageCode.USER_IDENTIFIER_REQUIRED,
				"Identifier for the user aggregate is required",
				"id");

		// Normalización ligera
		idType = UUIDHelper.getDefault(idType);
		homeCity = UUIDHelper.getDefault(homeCity);

		idNumber = TextHelper.getDefaultWithTrim(idNumber);
		firstName = TextHelper.getDefaultWithTrim(firstName);
		secondName = TextHelper.getDefaultWithTrim(secondName);
		firstSurname = TextHelper.getDefaultWithTrim(firstSurname);
		secondSurname = TextHelper.getDefaultWithTrim(secondSurname);
		email = TextHelper.getDefaultWithTrim(email);
		mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);

		emailConfirmationToken = TextHelper.getDefaultWithTrim(emailConfirmationToken);
		mobileConfirmationToken = TextHelper.getDefaultWithTrim(mobileConfirmationToken);
		emailConfirmationExpiresAt = sanitizeTimestamp(emailConfirmationExpiresAt);
		mobileConfirmationExpiresAt = sanitizeTimestamp(mobileConfirmationExpiresAt);

		// Invariantes mínimas (sin catálogo)
		if (UUIDHelper.getDefault().equals(idType)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.ID_TYPE_REQUIRED,
					"idType is required");
		}
		if (UUIDHelper.getDefault().equals(homeCity)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.HOME_CITY_REQUIRED,
					"homeCity is required");
		}
	}

	// === Factory original (SE MANTIENE). NO hace validaciones de rango.
	public static RegisterUserDomain fromInput(final RegisterUserInputDomain inputDomain) {
		if (ObjectHelper.isNull(inputDomain)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
					"Register user input domain is required");
		}
		return new RegisterUserDomain(
				UUID.randomUUID(),
				inputDomain.idType(),
				inputDomain.idNumber(),
				inputDomain.firstName(),
				inputDomain.secondName(),
				inputDomain.firstSurname(),
				inputDomain.secondSurname(),
				inputDomain.homeCity(),
				inputDomain.email(),
				inputDomain.mobileNumber(),
				TextHelper.getDefault(),
				null,
				TextHelper.getDefault(),
				null
		);
	}

	// === Factory recomendado: valida TODO usando parámetros del catálogo
	public static RegisterUserDomain fromInput(final RegisterUserInputDomain inputDomain,
											   final RegisterUserParameters p) {
		final var draft = fromInput(inputDomain);
		return draft.applyDynamicValidation(p); // devuelve instancia final (email/mobile normalizados)
	}

	// === Validación dinámica con catálogo (crea una nueva instancia si normaliza email/mobile)
	private RegisterUserDomain applyDynamicValidation(final RegisterUserParameters p) {
		// Documento
		ensureNonBlankAndRange("idNumber", idNumber, p.idMin(), p.idMax(),
				RegisterUserMessageCode.ID_NUMBER_REQUIRED,
				RegisterUserMessageCode.ID_NUMBER_TOO_SHORT,
				RegisterUserMessageCode.ID_NUMBER_TOO_LONG);

		// Nombres / Apellidos
		ensureNonBlankAndRange("firstName", firstName, p.firstNameMin(), p.firstNameMax(),
				RegisterUserMessageCode.FIRST_NAME_REQUIRED,
				RegisterUserMessageCode.FIRST_NAME_TOO_SHORT,
				RegisterUserMessageCode.FIRST_NAME_TOO_LONG);
		ensurePattern("firstName", firstName, p.namePattern(), RegisterUserMessageCode.FIRST_NAME_INVALID_FORMAT);

		ensureNonBlankAndRange("firstSurname", firstSurname, p.firstSurnameMin(), p.firstSurnameMax(),
				RegisterUserMessageCode.FIRST_SURNAME_REQUIRED,
				RegisterUserMessageCode.FIRST_SURNAME_TOO_SHORT,
				RegisterUserMessageCode.FIRST_SURNAME_TOO_LONG);
		ensurePattern("firstSurname", firstSurname, p.namePattern(), RegisterUserMessageCode.FIRST_SURNAME_INVALID_FORMAT);

		ensureRangeOptional("secondName", secondName, p.secondNameMin(), p.secondNameMax(),
				RegisterUserMessageCode.SECOND_NAME_TOO_SHORT,
				RegisterUserMessageCode.SECOND_NAME_TOO_LONG);
		ensurePattern("secondName", secondName, p.namePattern(), RegisterUserMessageCode.SECOND_NAME_INVALID_FORMAT);

		ensureRangeOptional("secondSurname", secondSurname, p.secondSurnameMin(), p.secondSurnameMax(),
				RegisterUserMessageCode.SECOND_SURNAME_TOO_SHORT,
				RegisterUserMessageCode.SECOND_SURNAME_TOO_LONG);
		ensurePattern("secondSurname", secondSurname, p.namePattern(), RegisterUserMessageCode.SECOND_SURNAME_INVALID_FORMAT);

		// Email (normaliza a lower)
		final String normalizedEmail = validateAndNormalizeEmail(email, p);

		// Móvil (opcional)
		final String normalizedMobile = validateAndNormalizeMobileOptional(mobileNumber, p);

		// Devuelve una nueva instancia con normalizaciones aplicadas
		return new RegisterUserDomain(
				id,
				idType,
				idNumber,
				firstName,
				secondName,
				firstSurname,
				secondSurname,
				homeCity,
				normalizedEmail,
				normalizedMobile,
				emailConfirmationToken,
				emailConfirmationExpiresAt,
				mobileConfirmationToken,
				mobileConfirmationExpiresAt
		);
	}

	// === Withers (sin cambios)
	public RegisterUserDomain withId(final UUID newId) {
		return new RegisterUserDomain(
				newId,
				idType,
				idNumber,
				firstName,
				secondName,
				firstSurname,
				secondSurname,
				homeCity,
				email,
				mobileNumber,
				emailConfirmationToken,
				emailConfirmationExpiresAt,
				mobileConfirmationToken,
				mobileConfirmationExpiresAt);
	}

	public RegisterUserDomain withConfirmationTokens(final ConfirmationTokens tokens) {
		if (ObjectHelper.isNull(tokens)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.CONFIRMATION_TOKEN_GENERATION_FAILED,
					"Confirmation tokens are required");
		}
		return new RegisterUserDomain(
				id,
				idType,
				idNumber,
				firstName,
				secondName,
				firstSurname,
				secondSurname,
				homeCity,
				email,
				mobileNumber,
				tokens.emailToken(),
				tokens.emailExpiresAt(),
				tokens.smsToken(),
				tokens.smsExpiresAt());
	}

	// === Helpers base (sin catálogo)
	private static UUID validateIdentifier(
			final UUID value,
			final String messageCode,
			final String message,
			final String fieldName) {
		final var sanitized = UUIDHelper.getDefault(value);
		if (UUIDHelper.getDefault().equals(sanitized)) {
			throw UcoChallengeApplicationException.create(messageCode, message, fieldName);
		}
		return sanitized;
	}

	private static LocalDateTime sanitizeTimestamp(final LocalDateTime value) {
		if (ObjectHelper.isNull(value) || LocalDateTime.MIN.equals(value)) {
			return null;
		}
		return value;
	}

	// === Helpers de validación (catálogo)
	private static void ensureNonBlankAndRange(
			final String field,
			final String value,
			final int min,
			final int max,
			final String requiredCode,
			final String tooShortCode,
			final String tooLongCode) {
		if (TextHelper.isEmptyApplyingTrim(value)) {
			throw UcoChallengeApplicationException.create(requiredCode, field);
		}
		final var v = value.strip();
		final int len = v.length();
		if (len < min) throw UcoChallengeApplicationException.create(tooShortCode, field);
		if (len > max) throw UcoChallengeApplicationException.create(tooLongCode, field);
	}

	private static void ensureRangeOptional(
			final String field,
			final String value,
			final int min,
			final int max,
			final String tooShortCode,
			final String tooLongCode) {
		if (TextHelper.isEmptyApplyingTrim(value)) return;
		final var v = value.strip();
		final int len = v.length();
		if (len < min) throw UcoChallengeApplicationException.create(tooShortCode, field);
		if (len > max) throw UcoChallengeApplicationException.create(tooLongCode, field);
	}

	private static void ensurePattern(
			final String field,
			final String value,
			final String regex,
			final String invalidFormatCode) {
		if (TextHelper.isEmptyApplyingTrim(value)) return;
		if (!Pattern.compile(regex).matcher(value.strip()).matches()) {
			throw UcoChallengeApplicationException.create(invalidFormatCode, field);
		}
	}

	private static String validateAndNormalizeEmail(final String email, final RegisterUserParameters p) {
		if (TextHelper.isEmptyApplyingTrim(email)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_REQUIRED, "email");
		}
		final var v = email.strip();
		final int len = v.length();
		if (len < p.emailMin()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_TOO_SHORT, "email");
		}
		if (len > p.emailMax()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_TOO_LONG, "email");
		}
		if (!Pattern.compile(p.emailPattern()).matcher(v).matches()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_INVALID, "email");
		}
		return v.toLowerCase(Locale.ROOT);
	}

	private static String validateAndNormalizeMobileOptional(final String mobile,
															 final RegisterUserParameters p) {
		if (TextHelper.isEmptyApplyingTrim(mobile)) {
			return TextHelper.getDefault(); // opcional
		}
		final var v = mobile.strip();
		final int len = v.length();
		if (len < p.mobileMin()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.MOBILE_TOO_SHORT, "mobileNumber");
		}
		if (len > p.mobileMax()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.MOBILE_TOO_LONG, "mobileNumber");
		}
		if (!Pattern.compile(p.mobilePattern()).matcher(v).matches()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.MOBILE_INVALID, "mobileNumber");
		}
		return v;
	}
}
