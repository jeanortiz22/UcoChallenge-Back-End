// messages-service/src/main/java/co/edu/uco/messageservice/controller/MessageController.java
package co.edu.uco.messageservice.controller;

import co.edu.uco.messageservice.catalog.Message;
import co.edu.uco.messageservice.catalog.MessageCatalog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/messages/api/v1/messages")
public class MessageController {

    private final MessageCatalog catalog;

    public MessageController(MessageCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    public ResponseEntity<Map<String, Message>> getAll() {
        return ResponseEntity.ok(catalog.getAll());
    }

    @GetMapping("/{key}")
    public ResponseEntity<Message> getByKey(@PathVariable String key) {
        Message msg = catalog.get(key); // ahora retorna Message (o null)
        if (msg == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/{key}")
    public ResponseEntity<Void> upsert(@PathVariable String key, @RequestBody Message message) {
        if (message == null || message.getKey() == null || !key.equals(message.getKey())) {
            return ResponseEntity.badRequest().build();
        }
        catalog.upsert(message); // ahora retorna Message internamente
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        if (catalog.remove(key)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
