package co.edu.uco.messageservice.catalog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.edu.uco.messageservice.catalog.persistence.MessageDocumentRepository;

@Component
public class MessageCatalogInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MessageCatalogInitializer.class);
    private final MessageDocumentRepository repository;

    public MessageCatalogInitializer(MessageDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Message> defaults = MessageCatalogDefaults.defaults();
        defaults.forEach(message -> repository.findById(message.getKey()).ifPresentOrElse(existing -> {
            // no-op; mantenemos la versión existente
        }, () -> {
            log.debug("Sembrando mensaje por defecto {}", message.getKey());
            repository.save(MessageMapper.toDocument(message));
        }));
    }
}