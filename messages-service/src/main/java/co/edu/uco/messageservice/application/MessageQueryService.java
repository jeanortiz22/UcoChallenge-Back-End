package co.edu.uco.messageservice.application;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.uco.messageservice.catalog.Message;
import co.edu.uco.messageservice.catalog.MessageCatalog;

@Service
public class MessageQueryService {

	private final MessageCatalog catalog;

    public MessageQueryService(MessageCatalog catalog) {
        this.catalog = catalog;
    }

    public Map<String, Message> getAll() {
        return Map.copyOf(catalog.getAll());
    }

    public Optional<Message> get(String key) {
        return catalog.get(key);
    }
}
