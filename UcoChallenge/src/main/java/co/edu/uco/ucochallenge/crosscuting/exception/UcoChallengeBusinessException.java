package co.edu.uco.ucochallenge.crosscuting.exception;

public class UcoChallengeBusinessException extends UcoChallengeException {

    private UcoChallengeBusinessException(
        final String messageCode,
        final String message,
        final Throwable cause,
        final Object... parameters) {

        super(messageCode, message, cause, parameters);
    }

    private UcoChallengeBusinessException(
        final String messageCode,
        final String message,
        final Object... parameters) {

        super(messageCode, message, parameters);
    }

    public static UcoChallengeBusinessException create(
        final String messageCode,
        final String message,
        final Object... parameters) {

        return new UcoChallengeBusinessException(messageCode, message, parameters);
    }

    public static UcoChallengeBusinessException create(
        final String messageCode,
        final String message,
        final Throwable cause,
        final Object... parameters) {

        return new UcoChallengeBusinessException(messageCode, message, cause, parameters);
    }
}