package co.edu.uco.ucochallenge.primary.controller;

/*import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.primary.controller.response.ResponseErrorType;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ApiExceptionHandler();
    }

    @Test
    void handleBusinessExceptionShouldReturnBadRequest() {
        final var exception = UcoChallengeBusinessException.create(
            "code-business",
            "Business message",
            "field");

        final var response = handler.handleBusinessException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals("Business message", body.getMessage());
        assertEquals("code-business", body.getMessageCode());
        assertEquals(ResponseErrorType.BUSINESS, body.getType());
        assertEquals(List.of("field"), body.getParameters());
    }

    @Test
    void handleApplicationExceptionShouldReturnInternalServerError() {
        final var exception = UcoChallengeApplicationException.create(
            "code-application",
            "Application message",
            "field-one",
            "field-two");

        final var response = handler.handleApplicationException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals("Application message", body.getMessage());
        assertEquals("code-application", body.getMessageCode());
        assertEquals(ResponseErrorType.APPLICATION, body.getType());
        assertEquals(List.of("field-one", "field-two"), body.getParameters());
    }

    @Test
    void handleTechnicalExceptionShouldReturnInternalServerError() {
        final var exception = UcoChallengeTechnicalException.create(
            "code-technical",
            "Technical message");

        final var response = handler.handleTechnicalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals("Technical message", body.getMessage());
        assertEquals("code-technical", body.getMessageCode());
        assertEquals(ResponseErrorType.TECHNICAL, body.getType());
        assertTrue(body.getParameters().isEmpty());
    }

    @Test
    void handleUnexpectedExceptionShouldReturnInternalServerError() {
        final var response = handler.handleUnexpectedException(new RuntimeException("Unexpected"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals("Unexpected", body.getMessage());
        assertEquals("", body.getMessageCode());
        assertEquals(ResponseErrorType.UNKNOWN, body.getType());
        assertTrue(body.getParameters().isEmpty());
    }
}
*/