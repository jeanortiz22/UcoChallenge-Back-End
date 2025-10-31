package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

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

	// ==== Límites (negocio) ====
	private static final int IDNUMBER_MIN = 5, IDNUMBER_MAX = 20;
	private static final int NAME_MIN = 1, NAME_MAX = 60;
	private static final int SURNAME_MIN = 1, SURNAME_MAX = 60;
	private static final int OPTIONAL_NAME_MIN = 1, OPTIONAL_NAME_MAX = 60;
	private static final int EMAIL_MIN = 5, EMAIL_MAX = 120;
	private static final int MOBILE_MIN = 7, MOBILE_MAX = 15;

	// ==== Patrones ====
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\+?\\d{7,15}$");
	private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");

	// === Constructor canónico ===
	public RegisterUserDomain {
		// id requerido
		id = validateIdentifier(
				id,
				RegisterUserMessageCode.USER_IDENTIFIER_REQUIRED,
				"Identifier for the user aggregate is required",
				"id");

		// Normalización ligera
		idType = UUIDHelper.getDefault(idType);
		idNumber = TextHelper.getDefaultWithTrim(idNumber);
		firstName = TextHelper.getDefaultWithTrim(firstName);
		secondName = TextHelper.getDefaultWithTrim(secondName);
		firstSurname = TextHelper.getDefaultWithTrim(firstSurname);
		secondSurname = TextHelper.getDefaultWithTrim(secondSurname);
		homeCity = UUIDHelper.getDefault(homeCity);
		email = TextHelper.getDefaultWithTrim(email);
		mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);
		emailConfirmationToken = TextHelper.getDefaultWithTrim(emailConfirmationToken);
		emailConfirmationExpiresAt = sanitizeTimestamp(emailConfirmationExpiresAt);
		mobileConfirmationToken = TextHelper.getDefaultWithTrim(mobileConfirmationToken);
		mobileConfirmationExpiresAt = sanitizeTimestamp(mobileConfirmationExpiresAt);

		// === Invariantes: idType y homeCity requeridos ===
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

		// ===== Reglas por campo =====
		// Documento: requerido con rango
		ensureNonBlankAndRange("idNumber", idNumber, IDNUMBER_MIN, IDNUMBER_MAX,
				RegisterUserMessageCode.ID_NUMBER_REQUIRED,
				RegisterUserMessageCode.ID_NUMBER_TOO_SHORT,
				RegisterUserMessageCode.ID_NUMBER_TOO_LONG);

		// Nombres/apellidos obligatorios
		ensureNonBlankAndRange("firstName", firstName, NAME_MIN, NAME_MAX,
				RegisterUserMessageCode.FIRST_NAME_REQUIRED,
				RegisterUserMessageCode.FIRST_NAME_TOO_SHORT,
				RegisterUserMessageCode.FIRST_NAME_TOO_LONG);
		ensureAlphabeticFormat("firstName", firstName, RegisterUserMessageCode.FIRST_NAME_INVALID_FORMAT);

		ensureNonBlankAndRange("firstSurname", firstSurname, SURNAME_MIN, SURNAME_MAX,
				RegisterUserMessageCode.FIRST_SURNAME_REQUIRED,
				RegisterUserMessageCode.FIRST_SURNAME_TOO_SHORT,
				RegisterUserMessageCode.FIRST_SURNAME_TOO_LONG);
		ensureAlphabeticFormat("firstSurname", firstSurname, RegisterUserMessageCode.FIRST_SURNAME_INVALID_FORMAT);


		// Campos opcionales: validar sólo si vienen con texto
		ensureRangeOptional("secondName", secondName, OPTIONAL_NAME_MIN, OPTIONAL_NAME_MAX,
				RegisterUserMessageCode.SECOND_NAME_TOO_SHORT,
				RegisterUserMessageCode.SECOND_NAME_TOO_LONG);
		ensureAlphabeticFormat("secondName", secondName, RegisterUserMessageCode.SECOND_NAME_INVALID_FORMAT);


		ensureRangeOptional("secondSurname", secondSurname, OPTIONAL_NAME_MIN, OPTIONAL_NAME_MAX,
				RegisterUserMessageCode.SECOND_SURNAME_TOO_SHORT,
				RegisterUserMessageCode.SECOND_SURNAME_TOO_LONG);
		ensureAlphabeticFormat("secondSurname", secondSurname, RegisterUserMessageCode.SECOND_SURNAME_INVALID_FORMAT);

		// Email: requerido, rango y formato; normalizar a lower
		email = validateAndNormalizeEmail(email);

		// Móvil: opcional; si viene validar rango y formato
		mobileNumber = validateAndNormalizeMobileOptional(mobileNumber);
	}

	// === Fábrica desde input ===
	public static RegisterUserDomain fromInput(final RegisterUserInputDomain inputDomain) {
		if (ObjectHelper.isNull(inputDomain)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
					"Register user input domain is required");
		}
		final var generatedId = UUID.randomUUID();

		return new RegisterUserDomain(
				generatedId,
				inputDomain.idType(),
				inputDomain.idNumber(),
				inputDomain.firstName(),
				inputDomain.secondName(),
				inputDomain.firstSurname(),
				inputDomain.secondSurname(),
				inputDomain.homeCity(),
				inputDomain.email(),
				inputDomain.mobileNumber(),
				TextHelper.getDefault(), // emailConfirmationToken
				null,                    // emailConfirmationExpiresAt
				TextHelper.getDefault(), // mobileConfirmationToken
				null                     // mobileConfirmationExpiresAt
		);
	}

	// === Withers inmutables ===

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

	// === Helpers ===

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

	/** Requerido: verifica no-blanco y que min <= len <= max */
	private static void ensureNonBlankAndRange(
			final String field,
			final String value,
			final int min,
			final int max,
			final String requiredCode,
			final String tooShortCode,
			final String tooLongCode) {
		if (TextHelper.isEmptyApplyingTrim(value)) {
			throw UcoChallengeApplicationException.create(requiredCode, field + " is required", field);
		}
		final var v = value.strip();
		final int len = v.length();
		if (len < min) {
			throw UcoChallengeApplicationException.create(tooShortCode,
					field + " is shorter than min length " + min, field);
		}
		if (len > max) {
			throw UcoChallengeApplicationException.create(tooLongCode,
					field + " exceeds max length " + max, field);
		}
	}

	/** Opcional: si viene con texto, valida que min <= len <= max */
	private static void ensureRangeOptional(
			final String field,
			final String value,
			final int min,
			final int max,
			final String tooShortCode,
			final String tooLongCode) {
		if (TextHelper.isEmptyApplyingTrim(value)) {
			return; // vacío: permitido
		}
		final var v = value.strip();
		final int len = v.length();
		if (len < min) {
			throw UcoChallengeApplicationException.create(tooShortCode,
					field + " is shorter than min length " + min, field);
		}
		if (len > max) {
			throw UcoChallengeApplicationException.create(tooLongCode,
					field + " exceeds max length " + max, field);
		}
	}


	/** Verifica que un nombre o apellido contenga solo letras y espacios */
	private static void ensureAlphabeticFormat(
			final String field,
			final String value,
			final String invalidFormatCode) {

		if (TextHelper.isEmptyApplyingTrim(value)) {
			return; // vacío permitido solo si el campo es opcional
		}

		if (!NAME_PATTERN.matcher(value.strip()).matches()) {
			throw UcoChallengeApplicationException.create(
					invalidFormatCode,
					field + " can only contain alphabetic characters and spaces",
					field);
		}
	}





	private static String validateAndNormalizeEmail(final String email) {
		if (TextHelper.isEmptyApplyingTrim(email)) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_REQUIRED, "email is required", "email");
		}
		final var v = email.strip();
		final int len = v.length();
		if (len < EMAIL_MIN) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_TOO_SHORT,
					"email is shorter than min length " + EMAIL_MIN, "email");
		}
		if (len > EMAIL_MAX) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_TOO_LONG,
					"email exceeds max length " + EMAIL_MAX, "email");
		}
		if (!EMAIL_PATTERN.matcher(v).matches()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.EMAIL_INVALID, "email format is invalid", "email");
		}
		return v.toLowerCase(Locale.ROOT);
	}

	private static String validateAndNormalizeMobileOptional(final String mobile) {
		if (TextHelper.isEmptyApplyingTrim(mobile)) {
			return TextHelper.getDefault(); // opcional
		}
		final var v = mobile.strip();
		final int len = v.length();
		if (len < MOBILE_MIN) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.MOBILE_TOO_SHORT,
					"mobileNumber is shorter than min length " + MOBILE_MIN, "mobileNumber");
		}
		if (len > MOBILE_MAX) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.MOBILE_TOO_LONG,
					"mobileNumber exceeds max length " + MOBILE_MAX, "mobileNumber");
		}
		if (!MOBILE_PATTERN.matcher(v).matches()) {
			throw UcoChallengeApplicationException.create(
					RegisterUserMessageCode.MOBILE_INVALID,
					"mobileNumber format is invalid. Use digits with optional leading '+'", "mobileNumber");
		}
		return v;
	}
}
