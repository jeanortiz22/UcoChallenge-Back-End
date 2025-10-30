package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.StateEntity;

@Repository
public interface SpringDataStateRepository extends JpaRepository<StateEntity, UUID> {

    List<StateEntity> findByCountry_Id(UUID countryId);
}