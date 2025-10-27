package co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uco.ucochallenge.application.Void;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.ListUsersUseCase;
import co.edu.uco.ucochallenge.user.listusers.application.port.out.ListUsersGateway;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryDomain;

@Service
public class ListUsersUseCaseImpl implements ListUsersUseCase {

    private final ListUsersGateway gateway;

    public ListUsersUseCaseImpl(final ListUsersGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public List<UserSummaryDomain> execute(final Void domain) {
        return gateway.findAll();
    }
}