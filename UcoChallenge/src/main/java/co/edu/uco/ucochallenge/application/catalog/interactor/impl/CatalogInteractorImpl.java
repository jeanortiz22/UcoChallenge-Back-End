package co.edu.uco.ucochallenge.application.catalog.interactor.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.ucochallenge.application.catalog.interactor.CatalogInteractor;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.CatalogItemDTO;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.CityDTO;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.StateDTO;
import co.edu.uco.ucochallenge.application.catalog.usecase.CatalogUseCase;

@Service
@Transactional(readOnly = true)
public class CatalogInteractorImpl implements CatalogInteractor {

    private final CatalogUseCase useCase;

    public CatalogInteractorImpl(final CatalogUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public List<CatalogItemDTO> listIdTypes() {
        return useCase.listIdTypes().stream()
                .map(domain -> new CatalogItemDTO(domain.getId(), domain.getName()))
                .toList();
    }

    @Override
    public List<CatalogItemDTO> listCountries() {
        return useCase.listCountries().stream()
                .map(domain -> new CatalogItemDTO(domain.getId(), domain.getName()))
                .toList();
    }

    @Override
    public List<StateDTO> listStates(final UUID countryId) {
        final var sanitizedCountryId = Objects.requireNonNull(countryId, "countryId is required");
        return useCase.listStates(sanitizedCountryId).stream()
                .map(domain -> new StateDTO(domain.getId(), domain.getName(), domain.getCountryId()))
                .toList();
    }

    @Override
    public List<CityDTO> listCities(final UUID stateId) {
        final var sanitizedStateId = Objects.requireNonNull(stateId, "stateId is required");
        return useCase.listCities(sanitizedStateId).stream()
                .map(domain -> new CityDTO(domain.getId(), domain.getName(), domain.getStateId()))
                .toList();
    }
    
}