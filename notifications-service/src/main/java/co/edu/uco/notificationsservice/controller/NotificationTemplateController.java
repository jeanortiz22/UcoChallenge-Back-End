package co.edu.uco.notificationsservice.controller;

import co.edu.uco.notificationsservice.catalog.NotificationCatalog;
import co.edu.uco.notificationsservice.catalog.NotificationTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notifications/api/v1/templates")
public class NotificationTemplateController {
	
	private final NotificationCatalog catalog;

    public NotificationTemplateController(NotificationCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    public ResponseEntity<Map<String, NotificationTemplate>> getAll() {
    	return ResponseEntity.ok(catalog.getAll());
    }

    @GetMapping("/{key}")
    public ResponseEntity<NotificationTemplate> getByKey(@PathVariable String key) {
    	return catalog.get(key)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{key}")
    public ResponseEntity<Void> upsert(@PathVariable String key, @RequestBody NotificationTemplate template) {
        if (template == null || template.getKey() == null || !key.equals(template.getKey())) {
            return ResponseEntity.badRequest().build();
        }
        catalog.upsert(template);
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