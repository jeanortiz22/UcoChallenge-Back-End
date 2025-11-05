package co.edu.uco.ucochallenge.infrastructure.primary.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.infrastructure.primary.controller.response.ResponseError;
import co.edu.uco.ucochallenge.infrastructure.primary.controller.response.ResponseErrorType;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.messages.MessageCatalogPort;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @Mock
    private MessageCatalogPort catalog;

    private ApiExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ApiExceptionHandler(catalog);
    }

    @Test
    void handleBusiness_shouldResolveMessageUsingCatalog() {
        final var exception = UcoChallengeBusinessException.create(
                "business.code",
                "ignored",
                "field");

        when(catalog.format(eq("business.code"), any(Locale.class), any(Object[].class)))
                .thenReturn("Mensaje de negocio");

        final ResponseEntity<ResponseError> response = handler.handleBusiness(exception, Locale.US);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        final ResponseError body = response.getBody();
        assertNotNull(body);
        assertEquals("Mensaje de negocio", body.getMessage());
        assertEquals("business.code", body.getMessageCode());
        assertEquals(ResponseErrorType.BUSINESS, body.getType());
        assertEquals(List.of("field"), body.getParameters());
    }

    @Test
    void handleApplication_shouldNormalizeCodeFromMessageWhenMissing() {
        final var exception = UcoChallengeApplicationException.create(
                "   ",
                "[app.error.code]",
                "param1");

        when(catalog.format(eq("app.error.code"), any(Locale.class), any(Object[].class)))
                .thenReturn("Mensaje normalizado");

        final ResponseEntity<ResponseError> response = handler.handleApplication(exception, Locale.CANADA);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        final ResponseError body = response.getBody();
        assertNotNull(body);
        assertEquals("app.error.code", body.getMessageCode());
        assertEquals("Mensaje normalizado", body.getMessage());
        assertEquals(List.of("param1"), body.getParameters());
        assertEquals(ResponseErrorType.APPLICATION, body.getType());
    }

    @Test
    void handleTechnical_whenCatalogFails_shouldFallbackToCodeMarker() {
        final var exception = UcoChallengeTechnicalException.create(
                "TECH.ERROR",
                "Technical failure");

        when(catalog.format(eq("TECH.ERROR"), any(Locale.class), any(Object[].class)))
                .thenThrow(new RuntimeException("catalog failure"));

        final ResponseEntity<ResponseError> response = handler.handleTechnical(exception, Locale.UK);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        final ResponseError body = response.getBody();
        assertNotNull(body);
        assertEquals("[TECH.ERROR]", body.getMessage());
        assertEquals("TECH.ERROR", body.getMessageCode());
        assertEquals(ResponseErrorType.TECHNICAL, body.getType());
    }

    @Test
    void handleOthers_shouldReturnUnexpectedErrorPayload() {
        when(catalog.format(eq("UNEXPECTED_ERROR"), any(Locale.class), any(Object[].class)))
                .thenReturn("Unexpected friendly message");

        final ResponseEntity<ResponseError> response = handler.handleOthers(new RuntimeException("boom"), Locale.FRANCE);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        final ResponseError body = response.getBody();
        assertNotNull(body);
        assertEquals("Unexpected friendly message", body.getMessage());
        assertEquals("UNEXPECTED_ERROR", body.getMessageCode());
        assertEquals(ResponseErrorType.UNKNOWN, body.getType());
        assertTrue(body.getParameters().isEmpty());

        verify(catalog).format(eq("UNEXPECTED_ERROR"), any(Locale.class), any(Object[].class));
    }
}