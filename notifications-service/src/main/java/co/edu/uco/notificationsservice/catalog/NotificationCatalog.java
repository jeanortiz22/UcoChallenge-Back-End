package co.edu.uco.notificationsservice.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.notificationsservice.catalog.persistence.NotificationTemplateDocumentRepository;


@Service
@Transactional(readOnly = true)
public class NotificationCatalog {
	
	
	private static final Logger log = LoggerFactory.getLogger(NotificationCatalog.class);
    private final NotificationTemplateDocumentRepository repository;

    public NotificationCatalog(NotificationTemplateDocumentRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "notificationTemplatesAll")
    public Map<String, NotificationTemplate> getAll() {
        Map<String, NotificationTemplate> templates = new LinkedHashMap<>();
        repository.findAll().forEach(document -> {
            NotificationTemplate template = NotificationTemplateMapper.toDomain(document);
            if (template != null) {
                templates.put(template.getKey(), template);
            }
        });
        return templates;
    }

    @Cacheable(cacheNames = "notificationTemplates", key = "#key", unless = "#result.isEmpty()")
    public Optional<NotificationTemplate> get(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        return repository.findById(normalize(key)).map(NotificationTemplateMapper::toDomain);
    }

    public List<NotificationTemplate> findBulk(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return List.of();
        }
        List<String> normalizedKeys = keys.stream()
                .filter(key -> key != null && !key.isBlank())
                .map(this::normalize)
                .distinct()
                .toList();
        if (normalizedKeys.isEmpty()) {
            return List.of();
        }
        List<NotificationTemplate> templates = new ArrayList<>();
        repository.findAllById(normalizedKeys).forEach(document -> {
            NotificationTemplate template = NotificationTemplateMapper.toDomain(document);
            if (template != null) {
            	templates.add(template);
            }
        });
        return templates;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "notificationTemplates", key = "#template.key"),
            @CacheEvict(cacheNames = "notificationTemplatesAll", allEntries = true)
    })
    public void upsert(NotificationTemplate template) {
        if (template == null) {
            throw new IllegalArgumentException("template is required");
        }
        log.debug("Guardando plantilla de notificación {}", template.getKey());
        repository.save(NotificationTemplateMapper.toDocument(template));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "notificationTemplates", key = "#key"),
            @CacheEvict(cacheNames = "notificationTemplatesAll", allEntries = true)
    })
    public boolean remove(String key) {
        if (key == null || key.isBlank()) {
            return false;
        }
        String normalized = normalize(key);
        if (!repository.existsById(normalized)) {
            return false;
        }
        log.debug("Eliminando plantilla de notificación {}", normalized);
        repository.deleteById(normalized);
        return true;
    }

    public boolean exists(String key) {
        if (key == null || key.isBlank()) {
            return false;
        }
        return repository.existsById(normalize(key));
    }

    private String normalize(String key) {
        return key == null ? null : key.trim();
    }
}