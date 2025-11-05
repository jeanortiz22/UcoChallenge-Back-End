package co.edu.uco.parametersservice.catalog;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.parametersservice.catalog.persistence.ParameterDocumentRepository;

@Service
@Transactional(readOnly = true)
public class ParameterCatalog {

    private static final Logger log = LoggerFactory.getLogger(ParameterCatalog.class);
    private final ParameterDocumentRepository repository;

    public ParameterCatalog(ParameterDocumentRepository repository) {
        this.repository = repository;
    }

    /**
     * Obtener todos los parámetros almacenados.
     * Se cachea el mapa completo para lecturas masivas.
     */
    @Cacheable(
            cacheNames = "parametersAll",
            unless = "#result == null || #result.isEmpty()"
    )
    public Map<String, Parameter> getAll() {
        Map<String, Parameter> parameters = new LinkedHashMap<>();
        repository.findAll().forEach(document -> {
            Parameter parameter = ParameterMapper.toDomain(document);
            if (parameter != null) {
                parameters.put(parameter.getKey(), parameter);
            }
        });
        return parameters;
    }

    /**
     * Obtener un parámetro por clave.
     * Se almacena en caché individual bajo su clave normalizada.
     */
    @Cacheable(
            cacheNames = "parameters",
            key = "#key != null ? #key.trim() : null",
            unless = "#result == null || (#result instanceof T(java.util.Optional) && !#result.isPresent())"
    )
    public Optional<Parameter> get(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        return repository.findById(normalize(key)).map(ParameterMapper::toDomain);
    }

    /**
     * Crear o actualizar un parámetro.
     * Se invalida tanto la caché individual como la caché total.
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "parameters", key = "#parameter.key"),
            @CacheEvict(cacheNames = "parametersAll", allEntries = true)
    })
    public void upsert(Parameter parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException("parameter is required");
        }
        log.debug("Guardando parámetro {}", parameter.getKey());
        repository.save(ParameterMapper.toDocument(parameter));
    }

    /**
     * Eliminar un parámetro por clave.
     * También invalida ambas cachés.
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "parameters", key = "#key"),
            @CacheEvict(cacheNames = "parametersAll", allEntries = true)
    })
    public boolean remove(String key) {
        if (key == null || key.isBlank()) {
            return false;
        }
        String normalized = normalize(key);
        if (!repository.existsById(normalized)) {
            return false;
        }
        log.debug("Eliminando parámetro {}", normalized);
        repository.deleteById(normalized);
        return true;
    }

    private String normalize(String key) {
        return key == null ? null : key.trim();
    }
}