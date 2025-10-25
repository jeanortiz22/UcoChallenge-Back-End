package co.edu.uco.ucochallenge.crosscuting.exception;

import java.util.Arrays;
import java.util.List;

import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

public abstract class UcoChallengeException extends RuntimeException {

    private final String messageCode;
    private final List<Object> parameters;

    protected UcoChallengeException(
        final String messageCode,
        final String message,
        final Throwable cause,
        final Object... parameters) {

        super(TextHelper.getDefault(message), cause);
        this.messageCode = TextHelper.getDefaultWithTrim(messageCode);
        this.parameters = buildParameters(parameters);
    }

    protected UcoChallengeException(
        final String messageCode,
        final String message,
        final Object... parameters) {

        this(messageCode, message, null, parameters);
    }

    public String getMessageCode() {
        return messageCode;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    private static List<Object> buildParameters(final Object[] parameters) {
        final var safeParameters = ObjectHelper.getDefault(parameters, new Object[0]);
        return List.copyOf(Arrays.asList(safeParameters));
    }
}