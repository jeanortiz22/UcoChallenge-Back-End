package co.edu.uco.ucochallenge.user.registeruser.application.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.IdTypeEntity;

@Mapper(componentModel = "spring")
public interface IdTypeReferenceMapper {

    IdTypeEntity toEntity(UUID id); 

    @ObjectFactory
    default IdTypeEntity newIdType(UUID id) {
        return new IdTypeEntity.Builder()
            .id(id)
            .build();
    }
}
