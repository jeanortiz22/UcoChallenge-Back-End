package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public record ExistingUserInformation(
        UUID id,
        UUID idType,
        String idNumber,
        String firstName,
        String firstSurname,
        String email,
        String mobileNumber) {

    public ExistingUserInformation {
        id = UUIDHelper.getDefault(id);
        idType = UUIDHelper.getDefault(idType);
        idNumber = TextHelper.getDefaultWithTrim(idNumber);
        firstName = TextHelper.getDefaultWithTrim(firstName);
        firstSurname = TextHelper.getDefaultWithTrim(firstSurname);
        email = TextHelper.getDefaultWithTrim(email);
        mobileNumber = TextHelper.getDefaultWithTrim(mobileNumber);
    }

    public String fullName() {
        var name = TextHelper.getDefaultWithTrim(firstName);
        var surname = TextHelper.getDefaultWithTrim(firstSurname);
        if (TextHelper.isEmpty(name) && TextHelper.isEmpty(surname)) {
            return "";
        }
        if (TextHelper.isEmpty(name)) {
            return surname;
        }
        if (TextHelper.isEmpty(surname)) {
            return name;
        }
        return name + " " + surname;
    }
}