package co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;

public final class CatalogItemDomain {

    private final UUID id;
    private final String name;

    private CatalogItemDomain(final UUID id, final String name) {
        this.id = UUIDHelper.getDefault(id);
        this.name = TextHelper.getDefaultWithTrim(name);
    }

    public static CatalogItemDomain create(final UUID id, final String name) {
        return new CatalogItemDomain(id, name);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}