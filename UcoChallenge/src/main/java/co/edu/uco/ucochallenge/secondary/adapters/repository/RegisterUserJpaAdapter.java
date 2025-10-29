package co.edu.uco.ucochallenge.secondary.adapters.repository;

import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.user.registeruser.application.mapper.RegisterUserEntityMapper;
@Component
public class RegisterUserJpaAdapter implements RegisterUserGateway {

    private final SpringDataUserRepository userRepository;
    private final RegisterUserEntityMapper entityMapper;

    public RegisterUserJpaAdapter(
    		final SpringDataUserRepository userRepository,
    		final RegisterUserEntityMapper entityMapper) {
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public void save(final RegisterUserDomain domain) {
    	final var userEntity = entityMapper.toEntity(domain);
        userRepository.save(userEntity);
    }
    
    @Override
    public boolean existsById(final UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByIdentification(final UUID idType, final String idNumber) {
        return userRepository.existsByIdTypeIdAndIdNumber(idType, idNumber);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByMobileNumber(final String mobileNumber) {
        return userRepository.existsByMobileNumber(mobileNumber);
    }

}
