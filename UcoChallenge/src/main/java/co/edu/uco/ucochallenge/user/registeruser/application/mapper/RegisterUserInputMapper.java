package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import org.mapstruct.Mapper;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserInputDomain;

@Mapper(componentModel = "spring")
public interface RegisterUserInputMapper {

	default RegisterUserInputDomain toDomain(final RegisterUserInputDTO dto) {
        if (dto == null) {
            return null;
        }

        return RegisterUserInputDomain.create(
                dto.idType(),
                dto.idNumber(),
                dto.firstName(),
                dto.secondName(),
                dto.firstSurname(),
                dto.secondSurname(),
                dto.homeCity(),
                dto.email(),
                dto.mobileNumber());
    }
}
