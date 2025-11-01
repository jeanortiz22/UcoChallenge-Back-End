package co.edu.uco.parametersservice.catalog.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("parameters")
public class ParameterDocument {

    @Id
    private String key;
    private String value;

    public ParameterDocument() {
    }

    public ParameterDocument(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}