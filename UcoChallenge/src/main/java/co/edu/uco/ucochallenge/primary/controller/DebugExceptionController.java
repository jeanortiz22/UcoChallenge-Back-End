package co.edu.uco.ucochallenge.primary.controller;

import org.springframework.web.bind.annotation.*;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeApplicationException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeBusinessException;
import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;

@RestController
@RequestMapping("/debug")
public class DebugExceptionController {

    // ✅ Prueba de excepción de negocio
    @GetMapping("/business")
    public String simulateBusiness(@RequestParam(defaultValue = "correo@ejemplo.com") String email) {
        throw UcoChallengeBusinessException.create("USER_NOT_FOUND", null, (Throwable) null, email);
    }

    // ✅ Prueba de excepción técnica
    @GetMapping("/technical")
    public String simulateTechnical() {
        throw UcoChallengeTechnicalException.create("DATABASE_CONNECTION_ERROR", null, (Throwable) null);
    }

    // ✅ Prueba de excepción de aplicación
    @GetMapping("/application")
    public String simulateApplication() {
        throw UcoChallengeApplicationException.create("SERVICE_UNAVAILABLE", null, (Throwable) null);
    }
}
