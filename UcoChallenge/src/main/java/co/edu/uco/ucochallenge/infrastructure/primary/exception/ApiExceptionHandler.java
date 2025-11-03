package co.edu.uco.ucochallenge.infrastructure.primary.exception;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.infrastructure.primary.controller.response.ResponseError;
import co.edu.uco.ucochallenge.infrastructure.primary.controller.response.ResponseErrorType;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.messages.MessageCatalogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

/**
 * Handler global que:
 *  1) Normaliza el "messageCode" (aun cuando venga dentro de ex.getMessage() como [x.y.z]).
 *  2) Resuelve el mensaje humano vía MessageCatalogPort.
 *  3) Devuelve un ResponseError uniforme para el front.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageCatalogPort catalog;

    public ApiExceptionHandler(final MessageCatalogPort catalog) {
        this.catalog = catalog;
    }

    // ---------- Mapeos por tipo de excepción ----------

    @ExceptionHandler(UcoChallengeBusinessException.class)
    public ResponseEntity<ResponseError> handleBusiness(final UcoChallengeBusinessException ex, final Locale locale) {
        return build(ex, ResponseErrorType.BUSINESS, HttpStatus.BAD_REQUEST, locale);
    }

    @ExceptionHandler(UcoChallengeTechnicalException.class)
    public ResponseEntity<ResponseError> handleTechnical(final UcoChallengeTechnicalException ex, final Locale locale) {
        return build(ex, ResponseErrorType.TECHNICAL, HttpStatus.INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(UcoChallengeApplicationException.class)
    public ResponseEntity<ResponseError> handleApplication(final UcoChallengeApplicationException ex, final Locale locale) {
        return build(ex, ResponseErrorType.APPLICATION, HttpStatus.BAD_REQUEST, locale);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleOthers(final Exception ex, final Locale locale) {
        log.error("Excepción no controlada: {}", ex.getMessage(), ex);
        var error = new ResponseError(
                resolveMessage("UNEXPECTED_ERROR", locale, List.of()),
                "UNEXPECTED_ERROR",
                List.of(),
                ResponseErrorType.UNKNOWN,
                Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ---------- Núcleo de normalización + resolución de mensajes ----------

    private ResponseEntity<ResponseError> build(final UcoChallengeException ex,
                                                final ResponseErrorType type,
                                                final HttpStatus status,
                                                final Locale locale) {

        // 1) Determinar el código real
        final String code = normalizeCode(ex);

        // 2) Resolver mensaje humano desde el catálogo
        final String message = resolveMessage(code, locale, ex.getParameters());

        final ResponseError body = new ResponseError(
                message,
                code,
                ex.getParameters(),
                type,
                Instant.now().toString()
        );
        return ResponseEntity.status(status).body(body);
    }

    /**
     * Si messageCode viene vacío, intenta extraer el código desde ex.getMessage():
     * Ej: "[user.register.firstName.required]" → "user.register.firstName.required".
     */
    private String normalizeCode(final UcoChallengeException ex) {
        if (ex.getMessageCode() != null && !ex.getMessageCode().isBlank()) {
            return ex.getMessageCode();
        }

        String raw = ex.getMessage();
        if (raw == null) {
            return "UNEXPECTED_ERROR";
        }

        raw = raw.trim();

        // Si viene con corchetes, los quitamos
        if (raw.startsWith("[") && raw.endsWith("]") && raw.length() >= 2) {
            raw = raw.substring(1, raw.length() - 1);
        }

        // ¿Tiene pinta de code tipo x.y.z?
        if (raw.matches("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$")) {
            return raw;
        }

        // Fallback
        return "UNEXPECTED_ERROR";
    }

    private String resolveMessage(final String code, final Locale locale, final List<Object> parameters) {
        final Object[] args = parameters == null ? new Object[]{} : parameters.toArray();
        try {
            final String formatted = catalog.format(code, locale, args);
            if (formatted == null || formatted.isBlank()) {
                return '[' + code + ']';
            }
            return formatted;
        } catch (Exception error) {
            log.error("Error formateando mensaje del catálogo. code={}, args={}, detalle={}",
                    code, parameters, error.getMessage());
            return '[' + code + ']';
        }
    }
}
