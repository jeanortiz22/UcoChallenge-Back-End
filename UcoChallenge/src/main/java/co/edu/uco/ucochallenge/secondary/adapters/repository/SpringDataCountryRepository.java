package co.edu.uco.ucochallenge.secondary.adapters.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CountryEntity;

@Repository
public interface SpringDataCountryRepository extends JpaRepository<CountryEntity, UUID> {
    List<CountryEntity> findAllByOrderByNameAsc();
}