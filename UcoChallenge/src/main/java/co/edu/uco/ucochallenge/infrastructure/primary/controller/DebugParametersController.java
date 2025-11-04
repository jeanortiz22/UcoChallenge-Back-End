package co.edu.uco.ucochallenge.infrastructure.primary.controller;

import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/debug/parameters")
public class DebugParametersController {

    private final ParameterCatalogPort parameterCatalog;
    private final WebClient parametersWebClient; // baseUrl debe apuntar al parameters-service (8082)

    public DebugParametersController(
            ParameterCatalogPort parameterCatalog,
            @Qualifier("parametersWebClient") WebClient parametersWebClient) {
        this.parameterCatalog = parameterCatalog;
        this.parametersWebClient = parametersWebClient;
    }

    // ✅ GET ALL directo al servicio (bypassa adapter)
    @GetMapping
    public ResponseEntity<String> getAllParameters() {
        try {
            String result = parametersWebClient.get()
                    .uri(b -> b.path("/parameters/api/v1/parameters").build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Error consultando todos los parámetros: " + e.getMessage());
        }
    }

    // ✅ GET por clave usando el PORT/ADAPTER (acepta puntos)
    @GetMapping("/{key:.+}")
    public ResponseEntity<String> getParameter(@PathVariable String key) {
        String value = parameterCatalog.get(key);
        if (!StringUtils.hasText(value)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    // ✅ GET por clave directo al servicio (bypassa adapter) – usa PATH, no query
    @GetMapping("/_one/{key:.+}")
    public ResponseEntity<String> getOneRaw(@PathVariable String key) {
        try {
            // encodéalo por si trae espacios o caracteres raros
            String enc = UriUtils.encodePathSegment(key, StandardCharsets.UTF_8);
            var res = parametersWebClient.get()
                    .uri("/parameters/api/v1/parameters/{k}", enc)
                    .exchangeToMono(r ->
                            r.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .map(b -> "STATUS=" + r.statusCode().value() + "\nBODY=" + b))
                    .block();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ONE ERROR: " + e.getMessage());
        }
    }

    // ✅ Ping directo al catálogo
    @GetMapping("/_ping")
    public ResponseEntity<String> ping() {
        try {
            var res = parametersWebClient.get()
                    .uri(b -> b.path("/parameters/api/v1/parameters").build())
                    .exchangeToMono(r ->
                            r.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .map(b -> "STATUS=" + r.statusCode().value() + "\nBODY=" + b))
                    .block();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("PING ERROR: " + e.getMessage());
        }
    }

    // (Opcional) GET con args a través del port
    @GetMapping("/{key:.+}/args")
    public ResponseEntity<String> getParameterWithArgs(
            @PathVariable String key,
            @RequestParam(required = false) String arg1,
            @RequestParam(required = false) String arg2) {

        String value = parameterCatalog.get(key, arg1, arg2);
        if (!StringUtils.hasText(value)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }
}
