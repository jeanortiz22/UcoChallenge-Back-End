package co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto;

import java.util.UUID;

public record StateDTO(UUID id, String name, UUID countryId) {
}