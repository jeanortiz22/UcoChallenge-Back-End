package co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.ExistingUserInformation;
import co.edu.uco.ucochallenge.user.registeruser.application.interactor.usecase.domain.RegisterUserDomain;

public interface RegisterUserGateway {

    void save(RegisterUserDomain domain);
    
    boolean existsById(UUID id);

    boolean existsByIdentification(UUID idType, String idNumber);
    
    Optional<ExistingUserInformation> findByIdentification(UUID idType, String idNumber);

    boolean existsByEmail(String email);
    
    Optional<ExistingUserInformation> findByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
    
    Optional<ExistingUserInformation> findByMobileNumber(String mobileNumber);
    
    boolean existsCity(UUID cityId);
}
