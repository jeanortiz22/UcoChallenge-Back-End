package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.UserEntity;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
	
	boolean existsByIdTypeIdAndIdNumber(UUID idType, String idNumber);

    Optional<UserEntity> findByIdTypeIdAndIdNumber(UUID idType, String idNumber);

    boolean existsByEmailIgnoreCase(String email);
    
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    boolean existsByMobileNumber(String mobileNumber);
    
    Optional<UserEntity> findByMobileNumber(String mobileNumber);
}
