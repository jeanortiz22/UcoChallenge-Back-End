package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.IdTypeEntity;

@Repository
public interface SpringDataIdTypeRepository extends JpaRepository<IdTypeEntity, UUID> {
    List<IdTypeEntity> findAllByOrderByNameAsc();
}