package co.edu.uco.ucochallenge.user.listusers.application.port.out;

import java.util.List;

import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryDomain;

public interface ListUsersGateway {

    List<UserSummaryDomain> findAll();
}