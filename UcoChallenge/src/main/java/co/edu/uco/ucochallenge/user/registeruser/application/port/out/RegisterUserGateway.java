package co.edu.uco.ucochallenge.user.registeruser.application.port.out;

import java.util.UUID;

import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public interface RegisterUserGateway {

    void save(RegisterUserDomain domain);

    boolean existsById(UUID id);

    boolean existsByIdentification(UUID idType, String idNumber);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
}
