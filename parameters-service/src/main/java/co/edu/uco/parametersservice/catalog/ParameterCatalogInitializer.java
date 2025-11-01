package co.edu.uco.parametersservice.catalog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.edu.uco.parametersservice.catalog.persistence.ParameterDocumentRepository;

@Component
public class ParameterCatalogInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ParameterCatalogInitializer.class);
    private final ParameterDocumentRepository repository;

    public ParameterCatalogInitializer(ParameterDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Parameter> defaults = ParameterCatalogDefaults.defaults();
        defaults.forEach(parameter -> repository.findById(parameter.getKey()).ifPresentOrElse(existing -> {
            // mantener valor actual
        }, () -> {
            log.debug("Sembrando parámetro por defecto {}", parameter.getKey());
            repository.save(ParameterMapper.toDocument(parameter));
        }));
    }
}