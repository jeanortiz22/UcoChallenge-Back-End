package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;

@Mapper(componentModel = "spring")
public interface IdTypeReferenceMapper {

    IdTypeEntity toEntity(UUID id); // MapStruct genera la implementación

    @ObjectFactory
    default IdTypeEntity newIdType(UUID id) {
        return new IdTypeEntity.Builder()
            .id(id)
            .build();
    }
}
