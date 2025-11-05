package co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.ListUsersGateway;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

class ListUsersUseCaseImplTest {

    private final ListUsersGateway gateway = mock(ListUsersGateway.class);
    private final ListUsersUseCaseImpl useCase = new ListUsersUseCaseImpl(gateway);

    @Test
    void executeShouldDelegateToGatewayWithNormalizedQuery() {
        final UserSummaryDomain summary = UserSummaryDomain.create(
                java.util.UUID.randomUUID(),
                java.util.UUID.randomUUID(),
                "123",
                "John",
                "A",
                "Doe",
                "B",
                java.util.UUID.randomUUID(),
                "john@example.com",
                "+573001234567",
                true,
                false);

        final UserSummaryPageDomain expected = UserSummaryPageDomain.create(List.of(summary), 1, 10, 20, 2);

        doReturn(expected).when(gateway).findAll(argThat(query -> {
            assertThat(query.getPage()).isEqualTo(1);
            assertThat(query.getSize()).isEqualTo(10);
            return true;
        }));

        final ListUsersQueryDomain input = ListUsersQueryDomain.create(1, 10);
        final UserSummaryPageDomain result = useCase.execute(input);

        assertThat(result).isSameAs(expected);
        verify(gateway).findAll(argThat(query -> query.getPage() == 1 && query.getSize() == 10));
    }
}