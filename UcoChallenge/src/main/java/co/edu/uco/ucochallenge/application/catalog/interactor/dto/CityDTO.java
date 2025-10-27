package co.edu.uco.ucochallenge.application.catalog.interactor.dto;

import java.util.UUID;

public record CityDTO(UUID id, String name, UUID stateId) {
}