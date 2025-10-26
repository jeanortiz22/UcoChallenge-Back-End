package co.edu.uco.ucochallenge.primary.controller;

import co.edu.uco.ucochallenge.crosscuting.messages.MessageCatalogPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug/messages")
public class DebugMessagesController {

    private final MessageCatalogPort messageCatalog;

    public DebugMessagesController(MessageCatalogPort messageCatalog) {
        this.messageCatalog = messageCatalog;
    }

    // ✅ mensaje simple
    @GetMapping("/{code}")
    public ResponseEntity<String> getMessage(@PathVariable String code) {
        String message = messageCatalog.format(code);
        return ResponseEntity.ok(message);
    }

    // ✅ mensaje con parámetros {0}, {1}, etc.
    @GetMapping("/{code}/args")
    public ResponseEntity<String> getMessageWithArgs(
            @PathVariable String code,
            @RequestParam(required = false) String arg1,
            @RequestParam(required = false) String arg2) {

        String message = messageCatalog.format(code, arg1, arg2);
        return ResponseEntity.ok(message);
    }
}
