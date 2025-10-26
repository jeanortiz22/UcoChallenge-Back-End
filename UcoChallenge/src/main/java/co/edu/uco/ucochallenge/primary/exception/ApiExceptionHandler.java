package co.edu.uco.ucochallenge.primary.exception;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.crosscuting.messages.MessageCatalogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageCatalogPort catalog;

    public ApiExceptionHandler(MessageCatalogPort catalog) {
        this.catalog = catalog;
    }

    @ExceptionHandler(UcoChallengeBusinessException.class)
    public ResponseEntity<?> handleBusiness(UcoChallengeBusinessException ex, Locale locale) {
        return build(extractCode(ex), extractParams(ex), HttpStatus.BAD_REQUEST, locale);
    }

    @ExceptionHandler(UcoChallengeTechnicalException.class)
    public ResponseEntity<?> handleTechnical(UcoChallengeTechnicalException ex, Locale locale) {
        return build(extractCode(ex), extractParams(ex), HttpStatus.INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(UcoChallengeApplicationException.class)
    public ResponseEntity<?> handleApplication(UcoChallengeApplicationException ex, Locale locale) {
        return build(extractCode(ex), extractParams(ex), HttpStatus.INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOthers(Exception ex) {
        log.error("Excepción no controlada: {}", ex.getMessage(), ex);
        var body = Map.of(
                "timestamp", Instant.now().toString(),
                "code", "UNEXPECTED_ERROR",
                "message", "Ha ocurrido un error inesperado."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // ---------- helpers ----------

    private String extractCode(Object ex) {
        try {
            // Ajusta al nombre real de tu getter si es distinto:
            return (String) ex.getClass().getMethod("getMessageCode").invoke(ex);
        } catch (Exception ignore) {
            try {
                return (String) ex.getClass().getMethod("getCode").invoke(ex);
            } catch (Exception e2) {
                log.warn("No se pudo extraer el code de la excepción, usando 'UNKNOWN_CODE'");
                return "UNKNOWN_CODE";
            }
        }
    }

    private Object[] extractParams(Object ex) {
        // Muchas jerarquías exponen List<Object> o Object[] o incluso null.
        try {
            var m = ex.getClass().getMethod("getParameters");
            var params = m.invoke(ex);
            if (params == null) return new Object[0];

            if (params instanceof List<?> list) {
                log.debug("Parámetros desde excepción (List): size={}", list.size());
                return list.toArray();
            }
            if (params.getClass().isArray()) {
                var arr = (Object[]) params;
                log.debug("Parámetros desde excepción (Array): size={}", arr.length);
                return arr;
            }
            // Cualquier otra cosa: lo mando como único argumento
            log.debug("Parámetros desde excepción (single): {}", params);
            return new Object[]{ params };
        } catch (NoSuchMethodException nsme) {
            log.debug("La excepción no expone getParameters(); usando vacío");
            return new Object[0];
        } catch (Exception e) {
            log.warn("Error obteniendo parámetros de la excepción: {}", e.getMessage());
            return new Object[0];
        }
    }

    private ResponseEntity<?> build(String code, Object[] params, HttpStatus status, Locale locale) {
        String message;
        try {
            log.debug("Formateando con catálogo. code={}, args={}", code, Arrays.toString(params));
            message = catalog.format(code, params);
            if (message == null || message.isBlank()) {
                message = "[" + code + "]";
            }
        } catch (IllegalArgumentException iae) {
            // mismatch placeholders ↔ args
            log.error("Mismatch al formatear: code={}, args={}, error={}", code, Arrays.toString(params), iae.getMessage());
            message = "[" + code + "]";
        } catch (Exception any) {
            log.error("Catálogo falló para code={}: {}", code, any.getMessage());
            message = "[" + code + "]";
        }

        var body = Map.of(
                "timestamp", Instant.now().toString(),
                "code", code,
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }
}
