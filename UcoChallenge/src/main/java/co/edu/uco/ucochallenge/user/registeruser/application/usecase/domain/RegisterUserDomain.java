package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

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
	    String mobileNumber) {

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
	    } 

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
            inputDomain.mobileNumber());
    
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
