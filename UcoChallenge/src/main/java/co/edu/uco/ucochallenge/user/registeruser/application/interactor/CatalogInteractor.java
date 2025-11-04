package co.edu.uco.ucochallenge.user.registeruser.application.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.CatalogItemDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.CityDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.StateDTO;

public interface CatalogInteractor {

    List<CatalogItemDTO> listIdTypes();

    List<CatalogItemDTO> listCountries();

    List<StateDTO> listStates(UUID countryId);

    List<CityDTO> listCities(UUID stateId);
    
    CityDTO getCity(UUID cityId);
    
}