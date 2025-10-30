package co.edu.uco.ucochallenge.user.confirmation.application.port.out;

import java.util.Optional;
import java.util.UUID;

import co.edu.uco.ucochallenge.user.confirmation.application.domain.UserConfirmationDomain;

public interface UserConfirmationGateway {

    Optional<UserConfirmationDomain> findById(UUID userId);

    void save(UserConfirmationDomain domain);
}