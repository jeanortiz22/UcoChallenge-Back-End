package co.edu.uco.ucochallenge.infrastructure.primary.controller;

import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/debug/parameters")
public class DebugParametersController {

    private final ParameterCatalogPort parameterCatalog;
    private final WebClient parametersWebClient; // lo usaremos para el GET ALL directo

    public DebugParametersController(
            ParameterCatalogPort parameterCatalog,
            @Qualifier("parametersWebClient") WebClient parametersWebClient) {
        this.parameterCatalog = parameterCatalog;
        this.parametersWebClient = parametersWebClient;
    }

    // ✅ obtener todos los parámetros directamente del servicio
    @GetMapping
    public ResponseEntity<String> getAllParameters() {
        try {
            String result = parametersWebClient.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Error consultando todos los parámetros: " + e.getMessage());
        }
    }

    // ✅ obtener un parámetro por su código
    @GetMapping("/{key}")
    public ResponseEntity<String> getParameter(@PathVariable String key) {
        String value = parameterCatalog.get(key);
        return ResponseEntity.ok(value);
    }

    // ✅ obtener un parámetro con argumentos opcionales
    @GetMapping("/{key}/args")
    public ResponseEntity<String> getParameterWithArgs(
            @PathVariable String key,
            @RequestParam(required = false) String arg1,
            @RequestParam(required = false) String arg2) {

        String value = parameterCatalog.get(key, arg1, arg2);
        return ResponseEntity.ok(value);
    }
    // ✅ Ping directo al catálogo (bypassa caché del adapter)
    @GetMapping("/_ping")
    public ResponseEntity<String> ping() {
        try {
            var res = parametersWebClient.get()
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




}
