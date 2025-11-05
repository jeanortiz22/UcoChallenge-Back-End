package co.edu.uco.ucochallenge.user.listusers.application.interactor.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.ListUsersRequestDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.dto.PagedUsersResponseDTO;
import co.edu.uco.ucochallenge.user.listusers.application.interactor.usecase.ListUsersUseCase;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.ListUsersQueryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

@ExtendWith(MockitoExtension.class)
class ListUsersInteractorImplTest {

    @Mock
    private ListUsersUseCase useCase;

    private ListUsersInteractorImpl interactor;

    @BeforeEach
    void setUp() {
        interactor = new ListUsersInteractorImpl(useCase);
    }

    @Test
    void execute_whenRequestIsNull_shouldNormalizeInputAndMapDomainToDto() {
        final var userId = UUID.randomUUID();
        final var idType = UUID.randomUUID();
        final var homeCity = UUID.randomUUID();
        final var domainUser = UserSummaryDomain.create(
                userId,
                idType,
                "CC123", // idNumber
                "Ana",
                "Maria",
                "Lopez",
                "Gomez",
                homeCity,
                "ana@example.com",
                "3001234567",
                true,
                false);

        final var pageDomain = UserSummaryPageDomain.create(
                List.of(domainUser),
                1,
                10,
                25L,
                3);

        when(useCase.execute(any(ListUsersQueryDomain.class))).thenReturn(pageDomain);

        final PagedUsersResponseDTO response = interactor.execute(null);

        assertNotNull(response);
        assertEquals(1, response.users().size());
        final var dto = response.users().get(0);
        assertEquals(domainUser.getId(), dto.id());
        assertEquals(domainUser.getEmail(), dto.email());
        assertEquals(domainUser.getMobileNumber(), dto.mobileNumber());
        assertEquals(domainUser.isEmailConfirmed(), dto.emailConfirmed());
        assertEquals(domainUser.isMobileNumberConfirmed(), dto.mobileNumberConfirmed());

        assertEquals(pageDomain.getTotalElements(), response.totalElements());
        assertEquals(pageDomain.getTotalPages(), response.totalPages());
        assertEquals(pageDomain.hasNext(), response.hasNext());
        assertEquals(pageDomain.hasPrevious(), response.hasPrevious());

        final ArgumentCaptor<ListUsersQueryDomain> queryCaptor = ArgumentCaptor.forClass(ListUsersQueryDomain.class);
        verify(useCase).execute(queryCaptor.capture());
        final ListUsersQueryDomain capturedQuery = queryCaptor.getValue();

        assertEquals(0, capturedQuery.getPage());
        assertEquals(20, capturedQuery.getSize());
        assertFalse(response.users().isEmpty());
    }

    @Test
    void execute_whenRequestHasValues_shouldClampAndFilterNullUsers() {
        final var userId = UUID.randomUUID();
        final var idType = UUID.randomUUID();
        final var homeCity = UUID.randomUUID();
        final var domainUser = UserSummaryDomain.create(
                userId,
                idType,
                "CC456",
                "Carlos",
                "",
                "Ramirez",
                "",
                homeCity,
                "carlos@example.com",
                "3007654321",
                false,
                true);

        final var pageDomain = UserSummaryPageDomain.create(
                Arrays.asList(domainUser, null),
                0,
                100,
                1L,
                1);

        when(useCase.execute(any(ListUsersQueryDomain.class))).thenReturn(pageDomain);

        final PagedUsersResponseDTO response = interactor.execute(new ListUsersRequestDTO(-5, 500));

        final ArgumentCaptor<ListUsersQueryDomain> queryCaptor = ArgumentCaptor.forClass(ListUsersQueryDomain.class);
        verify(useCase).execute(queryCaptor.capture());
        final ListUsersQueryDomain capturedQuery = queryCaptor.getValue();

        assertEquals(0, capturedQuery.getPage());
        assertEquals(100, capturedQuery.getSize());
        assertEquals(1, response.users().size());
        assertEquals(domainUser.getId(), response.users().get(0).id());
        assertEquals(pageDomain.getTotalElements(), response.totalElements());
        assertEquals(pageDomain.getTotalPages(), response.totalPages());
    }
}