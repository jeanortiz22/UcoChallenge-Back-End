package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;

@Mapper(
    componentModel = "spring",
    uses = { IdTypeReferenceMapper.class, CityReferenceMapper.class },
    builder = @Builder(disableBuilder = false)
)
public interface RegisterUserEntityMapper{

	@Mapping(source = "idType",   target = "idType")
    @Mapping(source = "homeCity", target = "homeCity")
    @Mapping(source = "emailConfirmationToken", target = "emailConfirmationToken")
    @Mapping(source = "emailConfirmationExpiresAt", target = "emailConfirmationExpiresAt")
    @Mapping(source = "mobileConfirmationToken", target = "mobileConfirmationToken")
    @Mapping(source = "mobileConfirmationExpiresAt", target = "mobileConfirmationExpiresAt") 
    @Mapping(target = "emailConfirmed", ignore = true)
    @Mapping(target = "mobileNumberConfirmed", ignore = true)
    

    UserEntity toEntity(RegisterUserDomain domain);
}
