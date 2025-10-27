package co.edu.uco.ucochallenge.application.catalog.usecase.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public final class CityDomain {

    private final UUID id;
    private final UUID stateId;
    private final String name;

    private CityDomain(final UUID id, final UUID stateId, final String name) {
        this.id = UUIDHelper.getDefault(id);
        this.stateId = UUIDHelper.getDefault(stateId);
        this.name = TextHelper.getDefaultWithTrim(name);
    }

    public static CityDomain create(final UUID id, final UUID stateId, final String name) {
        return new CityDomain(id, stateId, name);
    }

    public UUID getId() {
        return id;
    }

    public UUID getStateId() {
        return stateId;
    }

    public String getName() {
        return name;
    }
}