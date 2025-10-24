package co.edu.uco.parametersservice.controller;

import co.edu.uco.parametersservice.catalog.Parameter;
import co.edu.uco.parametersservice.catalog.ParameterCatalog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/parameters/api/v1/parameters")
public class ParameterController {

    // Obtener todos los parámetros
    @GetMapping
    public ResponseEntity<Map<String, Parameter>> getAllParameters() {
        return ResponseEntity.ok(ParameterCatalog.getAllParameters());
    }

    // Obtener un parámetro por su clave
    @GetMapping("/{key}")
    public ResponseEntity<Parameter> getParameterByKey(@PathVariable String key) {
        Parameter parameter = ParameterCatalog.getParameterValue(key);
        if (parameter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parameter);
    }

    // Actualizar o agregar un parámetro
    @PutMapping("/{key}")
    public ResponseEntity<Void> synchronizeParameter(@PathVariable String key, @RequestBody Parameter parameter) {
        if (!key.equals(parameter.getKey())) {
            return ResponseEntity.badRequest().build();
        }
        ParameterCatalog.synchronizeParameters(parameter);
        return ResponseEntity.noContent().build();
    }

    // Eliminar un parámetro
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteParameter(@PathVariable String key) {
        ParameterCatalog.removeParameter(key);
        return ResponseEntity.noContent().build();
    }
}
