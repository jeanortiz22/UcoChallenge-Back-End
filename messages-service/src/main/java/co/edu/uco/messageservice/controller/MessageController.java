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

    @GetMapping
    public ResponseEntity<Map<String, Message>> getAll() {
        return ResponseEntity.ok(MessageCatalog.getAll());
    }

    @GetMapping("/{key}")
    public ResponseEntity<Message> getByKey(@PathVariable String key) {
        return MessageCatalog.get(key)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{key}")
    public ResponseEntity<Void> upsert(@PathVariable String key, @RequestBody Message message) {
        if (message == null || message.getKey() == null || !key.equals(message.getKey())) {
            return ResponseEntity.badRequest().build();
        }
        MessageCatalog.upsert(message);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> delete(@PathVariable String key) {
        if (MessageCatalog.get(key).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        MessageCatalog.remove(key);
        return ResponseEntity.noContent().build();
    }
}
