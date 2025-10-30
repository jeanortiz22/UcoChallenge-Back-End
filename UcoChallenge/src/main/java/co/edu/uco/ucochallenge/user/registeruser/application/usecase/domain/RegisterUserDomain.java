package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.time.LocalDateTime;
import java.util.UUID;

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

		public RegisterUserDomain {
	        id = validateIdentifier(
	            id,
	            RegisterUserMessageCode.USER_IDENTIFIER_REQUIRED,
	            "Identifier for the user aggregate is required",
	            "id");
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
	        emailConfirmationExpiresAt = ObjectHelper.getDefault(emailConfirmationExpiresAt, LocalDateTime.MIN);
	        mobileConfirmationToken = TextHelper.getDefaultWithTrim(mobileConfirmationToken);
	        mobileConfirmationExpiresAt = ObjectHelper.getDefault(mobileConfirmationExpiresAt, LocalDateTime.MIN);
	    }

	    public static RegisterUserDomain fromInput(final RegisterUserInputDomain inputDomain) {
	        if (ObjectHelper.isNull(inputDomain)) {
	            throw UcoChallengeApplicationException.create(
	                RegisterUserMessageCode.INPUT_DOMAIN_REQUIRED,
	                "Register user input domain is required");
	        }

        return new RegisterUserDomain(
            inputDomain.getId(),
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
            LocalDateTime.MIN,
            TextHelper.getDefault(),
            LocalDateTime.MIN);

            }

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

}
