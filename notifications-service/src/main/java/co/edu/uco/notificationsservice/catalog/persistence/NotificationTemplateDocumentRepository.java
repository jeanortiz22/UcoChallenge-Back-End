package co.edu.uco.notificationsservice.catalog.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateDocumentRepository extends CrudRepository<NotificationTemplateDocument, String> {
}