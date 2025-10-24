package co.edu.uco.ucochallenge.secondary.adapters.repository;

import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.CityEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.IdTypeEntity;
import co.edu.uco.ucochallenge.secondary.adapters.repository.entity.UserEntity;
import co.edu.uco.ucochallenge.user.registeruser.application.port.out.RegisterUserGateway;
import co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain.RegisterUserDomain;

@Component
public class RegisterUserJpaAdapter implements RegisterUserGateway {

    private final SpringDataUserRepository userRepository;

    public RegisterUserJpaAdapter(final SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(final RegisterUserDomain domain) {
        final var userEntity = mapToEntity(domain);
        userRepository.save(userEntity);
    }

    private UserEntity mapToEntity(final RegisterUserDomain domain) {
        final var idType = new IdTypeEntity.Builder()
            .id(domain.getIdType())
            .build();

        final var homeCity = new CityEntity.Builder()
            .id(domain.getHomeCity())
            .build();

        return new UserEntity.Builder()
            .id(domain.getId())
            .idType(idType)
            .idNumber(domain.getIdNumber())
            .firstName(domain.getFirstName())
            .secondName(domain.getSecondName())
            .firstSurname(domain.getFirstSurname())
            .secondSurname(domain.getSecondSurname())
            .homeCity(homeCity)
            .email(domain.getEmail())
            .mobileNumber(domain.getMobileNumber())
            .build();
    }
}
