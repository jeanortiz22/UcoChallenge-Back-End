package co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.impl;


import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.ListUsersUseCase;
import co.edu.uco.ucochallenge.user.listusers.application.port.out.ListUsersGateway;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

@Service
public class ListUsersUseCaseImpl implements ListUsersUseCase {

    private final ListUsersGateway gateway;

    public ListUsersUseCaseImpl(final ListUsersGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public UserSummaryPageDomain execute(final ListUsersQueryDomain domain) {
        final var query = ListUsersQueryDomain.from(domain);
        return gateway.findAll(query);
    }
}