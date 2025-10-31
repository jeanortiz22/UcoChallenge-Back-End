package co.edu.uco.ucochallenge.user.confirmation.application.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public record UserConfirmationDomain(
        UUID id,
        UUID idType,
        String idNumber,
        String firstName,
        String firstSurname,
        String email,
        String mobileNumber,
        String emailConfirmationToken,
        LocalDateTime emailConfirmationExpiresAt,
        boolean emailConfirmed,
        String mobileConfirmationToken,
        LocalDateTime mobileConfirmationExpiresAt,
        boolean mobileNumberConfirmed) {

    public UserConfirmationDomain {
        id = UUIDHelper.getDefault(id);
        idType = UUIDHelper.getDefault(idType);
        idNumber = TextHelper.getDefaultWithTrim(idNumber);
        firstName = TextHelper.getDefaultWithTrim(firstName);
        firstSurname = TextHelper.getDefaultWithTrim(firstSurname);
        email = TextHelper.getDefaultWithTrim(email);
        mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);
        emailConfirmationToken = TextHelper.getDefaultWithTrim(emailConfirmationToken);
        emailConfirmationExpiresAt = sanitizeTimestamp(emailConfirmationExpiresAt);
        mobileConfirmationToken = TextHelper.getDefaultWithTrim(mobileConfirmationToken);
        mobileConfirmationExpiresAt = sanitizeTimestamp(mobileConfirmationExpiresAt);
    }

    public String displayName() {
        if (!TextHelper.isEmpty(firstName) && !TextHelper.isEmpty(firstSurname)) {
            return firstName + " " + firstSurname;
        }
        if (!TextHelper.isEmpty(firstName)) {
            return firstName;
        }
        if (!TextHelper.isEmpty(firstSurname)) {
            return firstSurname;
        }
        return "";
    }

    public boolean hasEmail() {
        return !TextHelper.isEmpty(email);
    }

    public boolean hasMobileNumber() {
        return !TextHelper.isEmpty(mobileNumber);
    }

    public UserConfirmationDomain withEmailConfirmationToken(final String token, final LocalDateTime expiresAt) {
        return new UserConfirmationDomain(
                id,
                idType,
                idNumber,
                firstName,
                firstSurname,
                email,
                mobileNumber,
                TextHelper.getDefaultWithTrim(token),
                sanitizeTimestamp(expiresAt),
                false,
                mobileConfirmationToken,
                mobileConfirmationExpiresAt,
                mobileNumberConfirmed);
    }

    public UserConfirmationDomain withMobileConfirmationToken(final String token, final LocalDateTime expiresAt) {
        return new UserConfirmationDomain(
                id,
                idType,
                idNumber,
                firstName,
                firstSurname,
                email,
                mobileNumber,
                emailConfirmationToken,
                emailConfirmationExpiresAt,
                emailConfirmed,
                TextHelper.getDefaultWithTrim(token),
                sanitizeTimestamp(expiresAt),
                false);
    }

    public UserConfirmationDomain confirmEmail() {
        return new UserConfirmationDomain(
                id,
                idType,
                idNumber,
                firstName,
                firstSurname,
                email,
                mobileNumber,
                TextHelper.getDefault(),
                null,
                true,
                mobileConfirmationToken,
                mobileConfirmationExpiresAt,
                mobileNumberConfirmed);
    }

    public UserConfirmationDomain confirmMobile() {
        return new UserConfirmationDomain(
                id,
                idType,
                idNumber,
                firstName,
                firstSurname,
                email,
                mobileNumber,
                emailConfirmationToken,
                emailConfirmationExpiresAt,
                emailConfirmed,
                TextHelper.getDefault(),
                null,
                true);
    }

    public boolean isAccountActivated() {
        return emailConfirmed && mobileNumberConfirmed;
    }
    
    private static LocalDateTime sanitizeTimestamp(final LocalDateTime value) {
        if (ObjectHelper.isNull(value) || LocalDateTime.MIN.equals(value)) {
            return null;
        }
        return value;
    }
}