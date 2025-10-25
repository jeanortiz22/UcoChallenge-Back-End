package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

public final class RegisterUserDomain {

    private final UUID id;
    private final UUID idType;
    private final String idNumber;
    private final String firstName;
    private final String secondName;
    private final String firstSurname;
    private final String secondSurname;
    private final UUID homeCity;
    private final String email;
    private final String mobileNumber;

    private RegisterUserDomain(
        final UUID id,
        final UUID idType,
        final String idNumber,
        final String firstName,
        final String secondName,
        final String firstSurname,
        final String secondSurname,
        final UUID homeCity,
        final String email,
        final String mobileNumber) {

        this.id = id;
        this.idType = idType;
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.homeCity = homeCity;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }
    

    public static RegisterUserDomain create(
        final UUID idType,
        final String idNumber,
        final String firstName,
        final String secondName,
        final String firstSurname,
        final String secondSurname,
        final UUID homeCity,
        final String email,
        final String mobileNumber) {

    	final var idTypeValidated = validateIdentifier(
                idType,
                RegisterUserMessageCode.ID_TYPE_REQUIRED,
                "Identification type is required",
                "idType");
    	final var idNumberValidated = validateRequiredText(
                idNumber,
                RegisterUserMessageCode.ID_NUMBER_REQUIRED,
                "Identification number is required",
                "idNumber");
    	final var firstNameValidated = validateRequiredText(
                firstName,
                RegisterUserMessageCode.FIRST_NAME_REQUIRED,
                "First name is required",
                "firstName");
        final var secondNameSanitized = TextHelper.getDefaultWithTrim(secondName);
        final var firstSurnameValidated = validateRequiredText(
                firstSurname,
                RegisterUserMessageCode.FIRST_SURNAME_REQUIRED,
                "First surname is required",
                "firstSurname");
        final var secondSurnameSanitized = TextHelper.getDefaultWithTrim(secondSurname);
        final var homeCityValidated = validateIdentifier(
                homeCity,
                RegisterUserMessageCode.HOME_CITY_REQUIRED,
                "Home city is required",
                "homeCity");
        final var emailValidated = validateRequiredText(
                email,
                RegisterUserMessageCode.EMAIL_REQUIRED,
                "Email is required",
                "email");
        final var mobileNumberValidated = validateRequiredText(
                mobileNumber,
                RegisterUserMessageCode.MOBILE_NUMBER_REQUIRED,
                "Mobile number is required",
                "mobileNumber");

        return new RegisterUserDomain(
            UUID.randomUUID(),
            idTypeValidated,
            idNumberValidated,
            firstNameValidated,
            secondNameSanitized,
            firstSurnameValidated,
            secondSurnameSanitized,
            homeCityValidated,
            emailValidated,
            mobileNumberValidated);
    }

    private static UUID validateIdentifier(
            final UUID value,
            final String messageCode,
            final String message,
            final String fieldName) {
        final var sanitized = UUIDHelper.getDefault(value);
        if (UUIDHelper.getDefault().equals(sanitized)) {
        	throw UcoChallengeBusinessException.create(messageCode, message, fieldName);
        }
        return sanitized;
    }

    private static String validateRequiredText(
            final String value,
            final String messageCode,
            final String message,
            final String fieldName) {
        final var sanitized = TextHelper.getDefaultWithTrim(value);
        if (TextHelper.isEmpty(sanitized)) {
        	throw UcoChallengeBusinessException.create(messageCode, message, fieldName);
        }
        return sanitized;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIdType() {
        return idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public UUID getHomeCity() {
        return homeCity;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
