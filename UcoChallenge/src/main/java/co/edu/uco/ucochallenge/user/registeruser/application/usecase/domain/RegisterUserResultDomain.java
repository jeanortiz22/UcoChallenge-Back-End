package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.util.Objects;
import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.messages.RegisterUserMessageCode;

public record RegisterUserResultDomain(UUID id, String messageCode) {

    public RegisterUserResultDomain {
        id = Objects.requireNonNull(id, "id must not be null");
        messageCode = TextHelper.getDefaultWithTrim(messageCode);
    }

    public static RegisterUserResultDomain success(final UUID id) {
        return new RegisterUserResultDomain(id, RegisterUserMessageCode.USER_REGISTERED_SUCCESSFULLY);
    }
}