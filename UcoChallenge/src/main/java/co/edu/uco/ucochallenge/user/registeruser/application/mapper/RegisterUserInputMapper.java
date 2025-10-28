package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.dto.RegisterUserInputDTO;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserInputDomain;

@Mapper(componentModel = "spring")
public interface RegisterUserInputMapper {

    @Mapping(target = "id", ignore = true) // El domain genera el ID internamente
    RegisterUserInputDomain toDomain(RegisterUserInputDTO dto);
}
