package co.edu.uco.ucochallenge.crosscuting.exception;

public class UcoChallengeTechnicalException extends UcoChallengeException {

    private UcoChallengeTechnicalException(
        final String messageCode,
        final String message,
        final Throwable cause,
        final Object... parameters) {

        super(messageCode, message, cause, parameters);
    }

    private UcoChallengeTechnicalException(
        final String messageCode,
        final String message,
        final Object... parameters) {

        super(messageCode, message, parameters);
    }

    public static UcoChallengeTechnicalException create(
        final String messageCode,
        final String message,
        final Object... parameters) {

        return new UcoChallengeTechnicalException(messageCode, message, parameters);
    }

    public static UcoChallengeTechnicalException create(
        final String messageCode,
        final String message,
        final Throwable cause,
        final Object... parameters) {

        return new UcoChallengeTechnicalException(messageCode, message, cause, parameters);
    }
}