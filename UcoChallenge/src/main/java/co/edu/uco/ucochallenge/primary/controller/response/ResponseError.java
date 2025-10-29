package co.edu.uco.ucochallenge.primary.controller.response;

import java.time.Instant;
import java.util.List;

import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

public final class ResponseError {

    private final String message;
    private final String messageCode;
    private final List<Object> parameters;
    private final ResponseErrorType type;
    private final String timestamp;

    // ✅ Constructor requerido por ApiExceptionHandler
    public ResponseError(
            final String message,
            final String messageCode,
            final List<Object> parameters,
            final ResponseErrorType type,
            final String timestamp) {
    	this.message = TextHelper.getDefaultWithTrim(message);
        this.messageCode = TextHelper.getDefaultWithTrim(messageCode);
        this.parameters = List.copyOf(ObjectHelper.getDefault(parameters, List.of()));
        this.type = ObjectHelper.getDefault(type, ResponseErrorType.UNKNOWN);
        this.timestamp = TextHelper.isEmpty(timestamp) ? Instant.now().toString() : TextHelper.getDefaultWithTrim(timestamp);
    }

    // 🧠 Si ya tienes factory methods, déjalos igual (no se rompen)
    public static ResponseError of(
            final String message,
            final String messageCode,
            final List<Object> parameters,
            final ResponseErrorType type) {
    	return new ResponseError(message, messageCode, parameters, type, Instant.now().toString());
    }

    public static ResponseError fromException(
            final co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException exception,
            final ResponseErrorType type) {
        return new ResponseError(
                exception.getMessage(),
                exception.getMessageCode(),
                exception.getParameters(),
                type,
                Instant.now().toString());
    }

    public String getMessage() { return message; }
    public String getMessageCode() { return messageCode; }
    public List<Object> getParameters() { return parameters; }
    public ResponseErrorType getType() { return type; }
    public String getTimestamp() { return timestamp; }
}
