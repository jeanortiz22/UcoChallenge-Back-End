package co.edu.uco.messageservice.catalog;

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

import co.edu.uco.messageservice.catalog.persistence.MessageDocumentRepository;

@Service
@Transactional(readOnly = true)
public class MessageCatalog {

    private static final Logger log = LoggerFactory.getLogger(MessageCatalog.class);
    private final MessageDocumentRepository repository;

    public MessageCatalog(MessageDocumentRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "messagesAll")
    public Map<String, Message> getAll() {
        Map<String, Message> messages = new LinkedHashMap<>();
        repository.findAll().forEach(document -> {
            Message message = MessageMapper.toDomain(document);
            if (message != null) {
                messages.put(message.getKey(), message);
            }
        });
        return messages;
    }

    @Cacheable(cacheNames = "messages", key = "#key", unless = "#result.isEmpty()")
    public Optional<Message> get(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        return repository.findById(normalize(key)).map(MessageMapper::toDomain);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "messages", key = "#message.key"),
            @CacheEvict(cacheNames = "messagesAll", allEntries = true),
            @CacheEvict(cacheNames = "messagesNotFound", key = "#message.key", condition = "#message != null")
    })
    
    public void upsert(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message is required");
        }
        log.debug("Persistiendo plantilla de mensaje {}", message.getKey());
        repository.save(MessageMapper.toDocument(message));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "messages", key = "#key"),
            @CacheEvict(cacheNames = "messagesAll", allEntries = true),
            @CacheEvict(cacheNames = "messagesNotFound", key = "#key")
    })
    public boolean remove(String key) {
        if (key == null || key.isBlank()) {
            return false;
        }
        String normalized = normalize(key);
        if (!repository.existsById(normalized)) {
            return false;
        }
        log.debug("Eliminando plantilla de mensaje {}", normalized);
        repository.deleteById(normalized);
        return true;
    }

    private String normalize(String key) {
        return key == null ? null : key.trim();
    }
}
