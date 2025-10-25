package co.edu.uco.ucochallenge.primary.controller.response;

import java.util.List;

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
        this.message = message;
        this.messageCode = messageCode;
        this.parameters = parameters;
        this.type = type;
        this.timestamp = timestamp;
    }

    // 🧠 Si ya tienes factory methods, déjalos igual (no se rompen)
    public static ResponseError of(
            final String message,
            final String messageCode,
            final List<Object> parameters,
            final ResponseErrorType type) {
        return new ResponseError(message, messageCode, parameters, type, java.time.Instant.now().toString());
    }

    public static ResponseError fromException(
            final co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException exception,
            final ResponseErrorType type) {
        return new ResponseError(
                exception.getMessage(),
                exception.getMessageCode(),
                exception.getParameters(),
                type,
                java.time.Instant.now().toString());
    }

    // ✅ Getters obligatorios para que se serialice en JSON
    public String getMessage() { return message; }
    public String getMessageCode() { return messageCode; }
    public List<Object> getParameters() { return parameters; }
    public ResponseErrorType getType() { return type; }
    public String getTimestamp() { return timestamp; }
}
