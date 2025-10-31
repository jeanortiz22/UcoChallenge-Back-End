package co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto;

import java.util.UUID;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para registro de usuario.
 * - Incluye validaciones de forma/sintaxis (Bean Validation).
 * - Incluye normalización ligera (trim, lower, defaults).
 * - Las reglas de negocio puras deben ir en el Dominio.
 */
public record RegisterUserInputDTO(
        UUID idType,
        String idNumber,
        String firstName,
        String secondName,
        String firstSurname,
        String secondSurname,
        UUID homeCity,
        String email,
        String mobileNumber
) {

    /**
     * Normaliza un DTO existente (seguro para null).
     */
    public static RegisterUserInputDTO normalize(final RegisterUserInputDTO dto) {
        if (dto == null) {
            return null;
        }
        return normalize(
                dto.idType(),
                dto.idNumber(),
                dto.firstName(),
                dto.secondName(),
                dto.firstSurname(),
                dto.secondSurname(),
                dto.homeCity(),
                dto.email(),
                dto.mobileNumber()
        );
    }

    /**
     * Normaliza cada campo aplicando defaults y trim.
     * No impone reglas de negocio; solo higiene de entrada.
     */
    public static RegisterUserInputDTO normalize(
            final UUID idType,
            final String idNumber,
            final String firstName,
            final String secondName,
            final String firstSurname,
            final String secondSurname,
            final UUID homeCity,
            final String email,
            final String mobileNumber
    ) {
        final var idTypeNormalized = UUIDHelper.getDefault(idType);

        final var idNumberNormalized     = TextHelper.getDefaultWithTrim(idNumber);
        final var firstNameNormalized    = TextHelper.getDefaultWithTrim(firstName);
        final var secondNameNormalized   = TextHelper.getDefaultWithTrim(secondName);
        final var firstSurnameNormalized = TextHelper.getDefaultWithTrim(firstSurname);
        final var secondSurnameNormalized= TextHelper.getDefaultWithTrim(secondSurname);
        final var homeCityNormalized     = UUIDHelper.getDefault(homeCity);

        // Email: trim + toLower para consistencia; TextHelper ya evita null
        final var emailNormalized        = TextHelper.getDefaultWithTrim(email).toLowerCase();
        final var mobileNumberNormalized = TextHelper.getDefaultWithTrim(mobileNumber);

        return new RegisterUserInputDTO(
                idTypeNormalized,
                idNumberNormalized,
                firstNameNormalized,
                secondNameNormalized,
                firstSurnameNormalized,
                secondSurnameNormalized,
                homeCityNormalized,
                emailNormalized,
                mobileNumberNormalized
        );
    }
}
