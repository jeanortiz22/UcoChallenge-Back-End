package co.edu.uco.ucochallenge.user.listusers.application.interactor.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        UUID idType,
        String idNumber,
        String firstName,
        String secondName,
        String firstSurname,
        String secondSurname,
        UUID homeCity,
        String email,
        String mobileNumber) {
}