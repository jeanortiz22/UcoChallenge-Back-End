package co.edu.uco.ucochallenge.user.listusers.application.interactor;

import co.edu.uco.ucochallenge.application.interactor.Interactor;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.ListUsersRequestDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.PagedUsersResponseDTO;

public interface ListUsersInteractor extends Interactor<ListUsersRequestDTO, PagedUsersResponseDTO> {
}
