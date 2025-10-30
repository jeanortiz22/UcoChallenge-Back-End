package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.CityEntity;

@Mapper(componentModel = "spring")
public interface CityReferenceMapper {

    CityEntity toEntity(UUID id);

    @ObjectFactory
    default CityEntity newCity(UUID id) {
        return new CityEntity.Builder()
            .id(id)
            .build();
    }
}
