package co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase;

import co.edu.uco.ucochallenge.application.interactor.usecase.UseCase;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

public interface ListUsersUseCase extends UseCase<ListUsersQueryDomain, UserSummaryPageDomain> {
}
