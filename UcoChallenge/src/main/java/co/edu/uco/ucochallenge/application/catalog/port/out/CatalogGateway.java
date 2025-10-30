package co.edu.uco.ucochallenge.application.catalog.port.out;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucochallenge.application.catalog.usecase.domain.CatalogItemDomain;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.CityDomain;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.StateDomain;

public interface CatalogGateway {

    List<CatalogItemDomain> listIdTypes();

    List<CatalogItemDomain> listCountries();

    List<StateDomain> listStates(UUID countryId);

    List<CityDomain> listCities(UUID stateId);
    
    CityDomain getCity(UUID cityId);
}