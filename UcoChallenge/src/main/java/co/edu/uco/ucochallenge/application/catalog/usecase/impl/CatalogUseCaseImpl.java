package co.edu.uco.ucochallenge.application.catalog.usecase.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.catalog.port.out.CatalogGateway;
import co.edu.uco.ucochallenge.application.catalog.usecase.CatalogUseCase;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.CatalogItemDomain;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.CityDomain;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.StateDomain;

@Service
public class CatalogUseCaseImpl implements CatalogUseCase {

    private final CatalogGateway gateway;

    public CatalogUseCaseImpl(final CatalogGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public List<CatalogItemDomain> listIdTypes() {
        return gateway.listIdTypes();
    }

    @Override
    public List<CatalogItemDomain> listCountries() {
        return gateway.listCountries();
    }

    @Override
    public List<StateDomain> listStates(final UUID countryId) {
        return gateway.listStates(countryId);
    }

    @Override
    public List<CityDomain> listCities(final UUID stateId) {
        return gateway.listCities(stateId);
    }
}