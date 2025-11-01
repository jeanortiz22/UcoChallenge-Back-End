package co.edu.uco.parametersservice.catalog;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Parameter {

    private final String key;
    private final String value;

    @JsonCreator
    public Parameter(@JsonProperty("key") String key,
                     @JsonProperty("value") String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("key is required");
        }
        this.key = key.trim();
        this.value = value == null ? "" : value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
