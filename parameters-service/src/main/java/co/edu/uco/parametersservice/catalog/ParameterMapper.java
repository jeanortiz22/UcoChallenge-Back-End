package co.edu.uco.parametersservice.catalog;

import co.edu.uco.parametersservice.catalog.persistence.ParameterDocument;

final class ParameterMapper {

    private ParameterMapper() {
    }

    static Parameter toDomain(ParameterDocument document) {
        if (document == null) {
            return null;
        }
        return new Parameter(document.getKey(), document.getValue());
    }

    static ParameterDocument toDocument(Parameter parameter) {
        if (parameter == null) {
            return null;
        }
        return new ParameterDocument(parameter.getKey(), parameter.getValue());
    }
}