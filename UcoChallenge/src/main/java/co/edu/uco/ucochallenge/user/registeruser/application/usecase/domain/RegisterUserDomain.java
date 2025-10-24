package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

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

        final var idTypeValidated = validateIdentifier(idType, "Identification type is required");
        final var idNumberValidated = validateRequiredText(idNumber, "Identification number is required");
        final var firstNameValidated = validateRequiredText(firstName, "First name is required");
        final var secondNameSanitized = TextHelper.getDefaultWithTrim(secondName);
        final var firstSurnameValidated = validateRequiredText(firstSurname, "First surname is required");
        final var secondSurnameSanitized = TextHelper.getDefaultWithTrim(secondSurname);
        final var homeCityValidated = validateIdentifier(homeCity, "Home city is required");
        final var emailValidated = validateRequiredText(email, "Email is required");
        final var mobileNumberValidated = validateRequiredText(mobileNumber, "Mobile number is required");

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

    private static UUID validateIdentifier(final UUID value, final String message) {
        final var sanitized = UUIDHelper.getDefault(value);
        if (UUIDHelper.getDefault().equals(sanitized)) {
            throw new IllegalArgumentException(message);
        }
        return sanitized;
    }

    private static String validateRequiredText(final String value, final String message) {
        final var sanitized = TextHelper.getDefaultWithTrim(value);
        if (TextHelper.isEmpty(sanitized)) {
            throw new IllegalArgumentException(message);
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
