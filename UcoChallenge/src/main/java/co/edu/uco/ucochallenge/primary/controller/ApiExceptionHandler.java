package co.edu.uco.ucochallenge.primary.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.primary.controller.response.ResponseError;
import co.edu.uco.ucochallenge.primary.controller.response.ResponseErrorType;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UcoChallengeBusinessException.class)
    public ResponseEntity<ResponseError> handleBusinessException(final UcoChallengeBusinessException exception) {
        return buildResponse(exception, HttpStatus.BAD_REQUEST, ResponseErrorType.BUSINESS);
    }

    @ExceptionHandler(UcoChallengeApplicationException.class)
    public ResponseEntity<ResponseError> handleApplicationException(final UcoChallengeApplicationException exception) {
        return buildResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, ResponseErrorType.APPLICATION);
    }

    @ExceptionHandler(UcoChallengeTechnicalException.class)
    public ResponseEntity<ResponseError> handleTechnicalException(final UcoChallengeTechnicalException exception) {
        return buildResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR, ResponseErrorType.TECHNICAL);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleUnexpectedException(final Exception exception) {
        final var response = ResponseError.of(
            TextHelper.getDefault(exception.getMessage()),
            TextHelper.EMPTY,
            List.of(),
            ResponseErrorType.UNKNOWN);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseError> buildResponse(
        final UcoChallengeException exception,
        final HttpStatus status,
        final ResponseErrorType type) {

        final var response = ResponseError.fromException(exception, type);
        return new ResponseEntity<>(response, status);
    }
}