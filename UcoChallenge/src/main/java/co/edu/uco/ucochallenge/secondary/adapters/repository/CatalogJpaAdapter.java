package co.edu.uco.ucochallenge.secondary.adapters.repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.application.catalog.port.out.CatalogGateway;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.CatalogItemDomain;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.CityDomain;
import co.edu.uco.ucochallenge.application.catalog.usecase.domain.StateDomain;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CountryEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.StateEntity;

@Component
public class CatalogJpaAdapter implements CatalogGateway {

    private final SpringDataIdTypeRepository idTypeRepository;
    private final SpringDataCountryRepository countryRepository;
    private final SpringDataStateRepository stateRepository;
    private final SpringDataCityRepository cityRepository;

    public CatalogJpaAdapter(
            final SpringDataIdTypeRepository idTypeRepository,
            final SpringDataCountryRepository countryRepository,
            final SpringDataStateRepository stateRepository,
            final SpringDataCityRepository cityRepository) {
        this.idTypeRepository = idTypeRepository;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<CatalogItemDomain> listIdTypes() {
        return idTypeRepository.findAll().stream()
                .map(this::mapIdType)
                .toList();
    }

    @Override
    public List<CatalogItemDomain> listCountries() {
        return countryRepository.findAll().stream()
                .map(this::mapCountry)
                .toList();
    }

    @Override
    public List<StateDomain> listStates(final UUID countryId) {
        final var sanitizedCountryId = Objects.requireNonNull(countryId, "countryId is required");
        return stateRepository.findByCountryId(sanitizedCountryId).stream()
                .map(this::mapState)
                .toList();
    }

    @Override
    public List<CityDomain> listCities(final UUID stateId) {
        final var sanitizedStateId = Objects.requireNonNull(stateId, "stateId is required");
        return cityRepository.findByStateId(sanitizedStateId).stream()
                .map(this::mapCity)
                .toList();
    }

    private CatalogItemDomain mapIdType(final IdTypeEntity entity) {
        return CatalogItemDomain.create(entity.getId(), entity.getName());
    }

    private CatalogItemDomain mapCountry(final CountryEntity entity) {
        return CatalogItemDomain.create(entity.getId(), entity.getName());
    }

    private StateDomain mapState(final StateEntity entity) {
        final var country = entity.getCountry();
        return StateDomain.create(
                entity.getId(),
                Objects.nonNull(country) ? country.getId() : null,
                entity.getName());
    }

    private CityDomain mapCity(final CityEntity entity) {
        final var state = entity.getState();
        return CityDomain.create(
                entity.getId(),
                Objects.nonNull(state) ? state.getId() : null,
                entity.getName());
    }
}