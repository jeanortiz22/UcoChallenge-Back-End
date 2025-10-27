package co.edu.uco.ucochallenge.user.listusers.application.interactor;

import java.util.List;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.application.interactor.Interactor;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.UserResponseDTO;

public interface ListUsersInteractor extends Interactor<Void, List<UserResponseDTO>> {
}