package co.edu.uco.ucochallenge.user.listusers.application.port.out;

import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

public interface ListUsersGateway {

    UserSummaryPageDomain findAll(ListUsersQueryDomain query);
}
