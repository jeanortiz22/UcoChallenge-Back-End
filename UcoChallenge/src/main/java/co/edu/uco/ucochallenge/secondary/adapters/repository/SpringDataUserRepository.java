package co.edu.uco.ucochallenge.secondary.adapters.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
}
