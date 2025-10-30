package co.edu.uco.ucochallenge.infrastructure.primary.exception;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.messages.MessageCatalogPort;
import co.edu.uco.ucochallenge.infrastructure.primary.controller.response.ResponseError;
import co.edu.uco.ucochallenge.infrastructure.primary.controller.response.ResponseErrorType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageCatalogPort catalog;

    public ApiExceptionHandler(MessageCatalogPort catalog) {
        this.catalog = catalog;
    }

    @ExceptionHandler(UcoChallengeBusinessException.class)
    public ResponseEntity<ResponseError> handleBusiness(UcoChallengeBusinessException ex, Locale locale) {
        return build(ex, ResponseErrorType.BUSINESS, HttpStatus.BAD_REQUEST, locale);
    }

    @ExceptionHandler(UcoChallengeTechnicalException.class)
    public ResponseEntity<ResponseError> handleTechnical(UcoChallengeTechnicalException ex, Locale locale) {
        return build(ex, ResponseErrorType.TECHNICAL, HttpStatus.INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(UcoChallengeApplicationException.class)
    public ResponseEntity<ResponseError> handleApplication(UcoChallengeApplicationException ex, Locale locale) {
        return build(ex, ResponseErrorType.APPLICATION, HttpStatus.INTERNAL_SERVER_ERROR, locale);
    }
    
 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleOthers(Exception ex, Locale locale) {
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

    
    private ResponseEntity<ResponseError> build(UcoChallengeException ex, ResponseErrorType type, HttpStatus status, Locale locale) {
        var error = new ResponseError(
                resolveMessage(ex.getMessageCode(), locale, ex.getParameters()),
                ex.getMessageCode(),
                ex.getParameters(),
                type,
                Instant.now().toString()
        );
        return ResponseEntity.status(status).body(error);
    }
    
    private String resolveMessage(String code, Locale locale, List<Object> parameters) {
    	var args = parameters.toArray();
        try {
        	var formatted = catalog.format(code, locale, args);
            if (formatted == null || formatted.isBlank()) {
                return '[' + code + ']';
            }
         return formatted;
    } catch (Exception error) {
        log.error("Error formateando mensaje del catálogo. code={}, args={}, detalle={}", code, parameters, error.getMessage());
        return '[' + code + ']';
        }
    }
}
