package co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository;

import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

public interface ListUsersGateway {

    UserSummaryPageDomain findAll(ListUsersQueryDomain query);

}