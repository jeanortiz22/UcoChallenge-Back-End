package co.edu.uco.parametersservice.catalog.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterDocumentRepository extends CrudRepository<ParameterDocument, String> {
}