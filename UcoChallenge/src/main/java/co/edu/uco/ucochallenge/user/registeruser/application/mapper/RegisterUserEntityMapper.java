package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

@Mapper(
    componentModel = "spring",
    uses = { IdTypeReferenceMapper.class, CityReferenceMapper.class },
    builder = @Builder(disableBuilder = false)
)
public interface RegisterUserEntityMapper {

    @Mapping(source = "idType",   target = "idType")     // UUID -> IdTypeEntity
    @Mapping(source = "homeCity", target = "homeCity")   // UUID -> CityEntity
    @Mapping(target = "emailConfirmed", ignore = true)
    @Mapping(target = "mobileNumberConfirmed", ignore = true)

    UserEntity toEntity(RegisterUserDomain domain);
}
