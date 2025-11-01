package co.edu.uco.messageservice.application;

import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import co.edu.uco.messageservice.catalog.Message;
import co.edu.uco.messageservice.catalog.MessageCatalog;

@Service
public class MessageQueryService {

    // Catálogo completo cacheado
    @Cacheable(cacheNames = "messagesAll")
    public Map<String, Message> getAll() {
        return Map.copyOf(MessageCatalog.getAll());
    }

    // Mensaje por clave cacheado (clave = #key)
    @Cacheable(cacheNames = "messages", key = "#key", unless = "#result.isEmpty()")
    public Optional<Message> get(String key) {
        return MessageCatalog.get(key);
    }

    // Marcador negativo (evita golpear backend si no existe)
    @Cacheable(cacheNames = "messagesNotFound", key = "#key")
    public boolean notFoundMarker(String key) {
        return true;
    }
}
