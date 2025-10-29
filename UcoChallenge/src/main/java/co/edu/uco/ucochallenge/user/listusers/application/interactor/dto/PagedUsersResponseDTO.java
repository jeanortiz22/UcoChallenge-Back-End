package co.edu.uco.ucochallenge.user.listusers.application.interactor.dto;

import java.util.List;
import java.util.Objects;

import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

public record PagedUsersResponseDTO(
        List<UserResponseDTO> users,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {

    public PagedUsersResponseDTO {
        users = List.copyOf(Objects.requireNonNullElse(users, List.of()));
        page = Math.max(page, 0);
        size = Math.max(size, 0);
        totalElements = Math.max(totalElements, 0L);
        totalPages = Math.max(totalPages, 0);

        final boolean computedHasNext = totalPages > 0 && page + 1 < totalPages;
        final boolean computedHasPrevious = totalPages > 0 && page > 0;

        hasNext = computedHasNext;
        hasPrevious = computedHasPrevious;
    }

    public static PagedUsersResponseDTO from(
            final UserSummaryPageDomain domain,
            final List<UserResponseDTO> users) {

        Objects.requireNonNull(domain, "domain must not be null");

        return new PagedUsersResponseDTO(
                users,
                domain.getPage(),
                domain.getSize(),
                domain.getTotalElements(),
                domain.getTotalPages(),
                domain.hasNext(),
                domain.hasPrevious());
    }
}
