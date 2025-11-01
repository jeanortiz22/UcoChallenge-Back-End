package co.edu.uco.messageservice.catalog.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDocumentRepository extends CrudRepository<MessageDocument, String> {
}