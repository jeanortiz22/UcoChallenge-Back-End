package co.edu.uco.ucochallenge.application.catalog.usecase.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public final class StateDomain {

    private final UUID id;
    private final UUID countryId;
    private final String name;

    private StateDomain(final UUID id, final UUID countryId, final String name) {
        this.id = UUIDHelper.getDefault(id);
        this.countryId = UUIDHelper.getDefault(countryId);
        this.name = TextHelper.getDefaultWithTrim(name);
    }

    public static StateDomain create(final UUID id, final UUID countryId, final String name) {
        return new StateDomain(id, countryId, name);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCountryId() {
        return countryId;
    }

    public String getName() {
        return name;
    }
}