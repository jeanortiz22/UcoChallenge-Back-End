package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.ExistingUserInformation;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;
import co.edu.uco.ucochallenge.crosscuting.helper.UUIDHelper;
import co.edu.uco.ucochallenge.user.registeruser.application.mapper.RegisterUserEntityMapper;
@Component
public class RegisterUserJpaAdapter implements RegisterUserGateway {

    private final SpringDataUserRepository userRepository;
    private final RegisterUserEntityMapper entityMapper;
    private final SpringDataCityRepository cityRepository;

    public RegisterUserJpaAdapter(
    		final SpringDataUserRepository userRepository,
    		final RegisterUserEntityMapper entityMapper,
    		final SpringDataCityRepository cityRepository) {
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
        this.cityRepository = cityRepository;
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
    public Optional<ExistingUserInformation> findByIdentification(final UUID idType, final String idNumber) {
        return userRepository.findByIdTypeIdAndIdNumber(idType, idNumber)
                .map(this::toExistingInformation);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }
    
    @Override
    public Optional<ExistingUserInformation> findByEmail(final String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(this::toExistingInformation);
    }

    @Override
    public boolean existsByMobileNumber(final String mobileNumber) {
        return userRepository.existsByMobileNumber(mobileNumber);
    }
    
    @Override
    public Optional<ExistingUserInformation> findByMobileNumber(final String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .map(this::toExistingInformation);
    }
    
    @Override
    public boolean existsCity(final UUID cityId) {
        return cityRepository.existsById(cityId);
    }
    
    private ExistingUserInformation toExistingInformation(final co.edu.uco.ucochallenge.infrastructure.secondary.adapters.repository.entity.UserEntity entity) {
        final var idTypeEntity = entity.getIdType();
        final var idType = idTypeEntity != null ? idTypeEntity.getId() : UUIDHelper.getDefault();
        return new ExistingUserInformation(
                entity.getId(),
                idType,
                entity.getIdNumber(),
                entity.getFirstName(),
                entity.getFirstSurname(),
                entity.getEmail(),
                entity.getMobileNumber());
    }

}
