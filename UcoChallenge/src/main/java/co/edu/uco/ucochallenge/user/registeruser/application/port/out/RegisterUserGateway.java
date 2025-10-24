package co.edu.uco.ucochallenge.user.registeruser.application.port.out;

import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

public interface RegisterUserGateway {

    void save(RegisterUserDomain domain);
}
