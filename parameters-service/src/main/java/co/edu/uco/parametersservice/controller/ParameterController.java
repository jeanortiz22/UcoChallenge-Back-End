package co.edu.uco.parametersservice.controller;

import co.edu.uco.parametersservice.catalog.Parameter;
import co.edu.uco.parametersservice.catalog.ParameterCatalog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/parameters/api/v1/parameters")
public class ParameterController {

    private final ParameterCatalog catalog;

    public ParameterController(ParameterCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    public ResponseEntity<Map<String, Parameter>> getAllParameters() {
        return ResponseEntity.ok(catalog.getAll());
    }

    // 🔧 Aceptar puntos en la key
    @GetMapping("/{key:.+}")
    public ResponseEntity<Parameter> getParameterByKey(@PathVariable String key) {
        return catalog.get(key)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{key:.+}")
    public ResponseEntity<Void> synchronizeParameter(@PathVariable String key, @RequestBody Parameter parameter) {
        if (parameter == null || parameter.getKey() == null || !key.equals(parameter.getKey())) {
            return ResponseEntity.badRequest().build();
        }
        catalog.upsert(parameter);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{key:.+}")
    public ResponseEntity<Void> deleteParameter(@PathVariable String key) {
        if (catalog.remove(key)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
