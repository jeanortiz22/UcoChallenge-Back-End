package co.edu.uco.ucochallenge.infrastructure.primary.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.ucochallenge.application.catalog.interactor.CatalogInteractor;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.CatalogItemDTO;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.CityDTO;
import co.edu.uco.ucochallenge.application.catalog.interactor.dto.StateDTO;

@RestController
@RequestMapping("/api/v1/catalogo")
public class CatalogController {

    private final CatalogInteractor catalogInteractor;

    public CatalogController(final CatalogInteractor catalogInteractor) {
        this.catalogInteractor = catalogInteractor;
    }

    @GetMapping("/tipos-documento")
    public ResponseEntity<List<CatalogItemDTO>> listIdTypes() {
        final var response = catalogInteractor.listIdTypes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paises")
    public ResponseEntity<List<CatalogItemDTO>> listCountries() {
        final var response = catalogInteractor.listCountries();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/departamentos")
    public ResponseEntity<List<StateDTO>> listStates(@RequestParam("paisId") final UUID countryId) {
        final var response = catalogInteractor.listStates(countryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ciudades")
    public ResponseEntity<List<CityDTO>> listCities(@RequestParam("departamentoId") final UUID stateId) {
        final var response = catalogInteractor.listCities(stateId);
        return ResponseEntity.ok(response);
    }
}
