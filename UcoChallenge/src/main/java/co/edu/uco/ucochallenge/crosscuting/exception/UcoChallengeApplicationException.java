package co.edu.uco.ucochallenge.crosscuting.exception;

public class UcoChallengeApplicationException extends UcoChallengeException {

    private UcoChallengeApplicationException(
        final String messageCode,
        final String message,
        final Throwable cause,
        final Object... parameters) {

        super(messageCode, message, cause, parameters);
    }

    private UcoChallengeApplicationException(
        final String messageCode,
        final String message,
        final Object... parameters) {

        super(messageCode, message, parameters);
    }

    public static UcoChallengeApplicationException create(
        final String messageCode,
        final String message,
        final Object... parameters) {

        return new UcoChallengeApplicationException(messageCode, message, parameters);
    }

    public static UcoChallengeApplicationException create(
        final String messageCode,
        final String message,
        final Throwable cause,
        final Object... parameters) {

        return new UcoChallengeApplicationException(messageCode, message, cause, parameters);
    }
}