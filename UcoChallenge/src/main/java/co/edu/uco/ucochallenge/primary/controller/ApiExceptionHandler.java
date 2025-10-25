package co.edu.uco.ucochallenge.primary.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import co.edu.uco.ucochallenge.crosscuting.exception.*;
import co.edu.uco.ucochallenge.crosscuting.messages.MessageCatalogPort;
import co.edu.uco.ucochallenge.primary.controller.response.ResponseError;
import co.edu.uco.ucochallenge.primary.controller.response.ResponseErrorType;

@ControllerAdvice
public class ApiExceptionHandler {

    private final MessageCatalogPort catalog;

    // ✅ Constructor MANUAL para inyección por constructor
    public ApiExceptionHandler(MessageCatalogPort catalog) {
        this.catalog = catalog;
    }

    @ExceptionHandler(UcoChallengeBusinessException.class)
    public ResponseEntity<ResponseError> handleBusiness(final UcoChallengeBusinessException exception) {
        return buildResponse(exception, HttpStatus.BAD_REQUEST, ResponseErrorType.BUSINESS);
    }

    @ExceptionHandler(UcoChallengeApplicationException.class)
    public ResponseEntity<ResponseError> handleApplication(final UcoChallengeApplicationException exception) {
        return buildResponse(exception, HttpStatus.BAD_REQUEST, ResponseErrorType.APPLICATION);
    }

    @ExceptionHandler(UcoChallengeTechnicalException.class)
    public ResponseEntity<ResponseError> handleTechnical(final UcoChallengeTechnicalException exception) {
        return buildResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, ResponseErrorType.TECHNICAL);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleUnexpected(final Exception exception) {
        var body = new ResponseError(
                "Ha ocurrido un error inesperado.",
                "UNEXPECTED_ERROR",
                List.of(),
                ResponseErrorType.UNKNOWN,
                Instant.now().toString()
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseError> buildResponse(
            final UcoChallengeException ex,
            final HttpStatus status,
            final ResponseErrorType type) {

        var resolved = catalog.format(ex.getMessageCode(), ex.getParameters().toArray());

        var body = new ResponseError(
                resolved,
                ex.getMessageCode(),
                ex.getParameters(),
                type,
                Instant.now().toString()
        );
        return new ResponseEntity<>(body, status);
    }
}
