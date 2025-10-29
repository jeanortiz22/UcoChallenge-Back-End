package co.edu.uco.ucochallenge.application.catalog.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucochallenge.application.catalog.interactor.dto.CatalogItemDTO;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.CityDTO;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.StateDTO;

public interface CatalogInteractor {

    List<CatalogItemDTO> listIdTypes();

    List<CatalogItemDTO> listCountries();

    List<StateDTO> listStates(UUID countryId);

    List<CityDTO> listCities(UUID stateId);
    
}