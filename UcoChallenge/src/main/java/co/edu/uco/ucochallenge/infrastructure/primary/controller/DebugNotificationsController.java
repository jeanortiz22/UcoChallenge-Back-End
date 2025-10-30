package co.edu.uco.ucochallenge.infrastructure.primary.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/debug/notifications")
public class DebugNotificationsController {

    private final WebClient notificationsClient;

    public DebugNotificationsController(@Qualifier("notificationsWebClient") WebClient notificationsClient) {
        this.notificationsClient = notificationsClient;
    }

    // ✅ Obtener todos los templates
    @GetMapping("/templates")
    public ResponseEntity<String> getAllTemplates() {
        String body = notificationsClient.get()
                .uri("/templates")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("❌ Error consultando templates")
                .block();
        return ResponseEntity.ok(body);
    }

    // ✅ Obtener template por key
    @GetMapping("/templates/{key}")
    public ResponseEntity<String> getTemplate(@PathVariable String key) {
        String body = notificationsClient.get()
                .uri("/templates/{k}", key)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("❌ Error consultando template " + key)
                .block();
        return ResponseEntity.ok(body);
    }
}
