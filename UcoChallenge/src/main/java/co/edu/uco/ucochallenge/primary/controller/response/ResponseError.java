package co.edu.uco.ucochallenge.primary.controller.response;

import java.util.List;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException;
import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

public final class ResponseError {

    private final String message;
    private final String messageCode;
    private final List<Object> parameters;
    private final ResponseErrorType type;

    private ResponseError(
        final String message,
        final String messageCode,
        final List<Object> parameters,
        final ResponseErrorType type) {

        this.message = TextHelper.getDefault(message);
        this.messageCode = TextHelper.getDefaultWithTrim(messageCode);
        this.parameters = List.copyOf(ObjectHelper.getDefault(parameters, List.of()));
        this.type = ObjectHelper.getDefault(type, ResponseErrorType.UNKNOWN);
    }

    public static ResponseError fromException(
        final UcoChallengeException exception,
        final ResponseErrorType type) {

        return new ResponseError(
            exception.getMessage(),
            exception.getMessageCode(),
            exception.getParameters(),
            type);
    }

    public static ResponseError of(
        final String message,
        final String messageCode,
        final List<Object> parameters,
        final ResponseErrorType type) {

        return new ResponseError(message, messageCode, parameters, type);
    }

    public String getMessage() {
        return message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public ResponseErrorType getType() {
        return type;
    }
}