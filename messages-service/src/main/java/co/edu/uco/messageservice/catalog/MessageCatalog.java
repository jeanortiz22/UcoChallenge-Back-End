// messages-service/src/main/java/co/edu/uco/messageservice/catalog/MessageCatalog.java
package co.edu.uco.messageservice.catalog;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageCatalog {

    // Simulación en memoria (cámbialo por RedisRepository si ya lo tienes)
    private final Map<String, Message> store = new HashMap<>();

    public MessageCatalog() {
        // carga defaults (tu clase MessageCatalogDefaults)
        for (Message m : MessageCatalogDefaults.defaults()) {
            store.put(m.getKey(), m);
        }
    }

    public Map<String, Message> getAll() {
        return Collections.unmodifiableMap(store);
    }

    @Cacheable(
            cacheNames = "message-templates",
            key = "#key",
            unless = "#result == null || #result.template == null || #result.template.isBlank()"
    )
    public Message get(String key) {
        // Recupera de Redis/DB. Aquí en memoria:
        return store.get(key);
    }

    @CachePut(
            cacheNames = "message-templates",
            key = "#message.key",
            unless = "#result == null || #result.template == null || #result.template.isBlank()"
    )
    public Message upsert(Message message) {
        // Persiste en Redis/DB. Aquí en memoria:
        store.put(message.getKey(), message);
        return message; // IMPORTANTE: CachePut necesita retornar el objeto
    }

    public boolean remove(String key) {
        return store.remove(key) != null;
    }
}
