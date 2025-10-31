package co.edu.uco.ucochallenge.user.listusers.application.usecase.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public final class UserSummaryDomain {

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
    private final boolean emailConfirmed;
    private final boolean mobileNumberConfirmed;

    private UserSummaryDomain(
            final UUID id,
            final UUID idType,
            final String idNumber,
            final String firstName,
            final String secondName,
            final String firstSurname,
            final String secondSurname,
            final UUID homeCity,
            final String email,
            final String mobileNumber,
            final boolean emailConfirmed,
            final boolean mobileNumberConfirmed) {

        this.id = UUIDHelper.getDefault(id);
        this.idType = UUIDHelper.getDefault(idType);
        this.idNumber = TextHelper.getDefaultWithTrim(idNumber);
        this.firstName = TextHelper.getDefaultWithTrim(firstName);
        this.secondName = TextHelper.getDefaultWithTrim(secondName);
        this.firstSurname = TextHelper.getDefaultWithTrim(firstSurname);
        this.secondSurname = TextHelper.getDefaultWithTrim(secondSurname);
        this.homeCity = UUIDHelper.getDefault(homeCity);
        this.email = TextHelper.getDefaultWithTrim(email);
        this.mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);
        this.emailConfirmed = emailConfirmed;
        this.mobileNumberConfirmed = mobileNumberConfirmed;
    }

    public static UserSummaryDomain create(
            final UUID id,
            final UUID idType,
            final String idNumber,
            final String firstName,
            final String secondName,
            final String firstSurname,
            final String secondSurname,
            final UUID homeCity,
            final String email,
            final String mobileNumber,
            final boolean emailConfirmed,
            final boolean mobileNumberConfirmed) {

        return new UserSummaryDomain(
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
                emailConfirmed,
                mobileNumberConfirmed);
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
    
    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public boolean isMobileNumberConfirmed() {
        return mobileNumberConfirmed;
    }
}