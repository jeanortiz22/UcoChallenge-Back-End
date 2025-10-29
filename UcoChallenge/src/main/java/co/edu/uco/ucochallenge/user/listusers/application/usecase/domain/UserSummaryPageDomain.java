package co.edu.uco.ucochallenge.user.listusers.application.usecase.domain;

import java.util.List;
import java.util.Objects;

public final class UserSummaryPageDomain {

    private final List<UserSummaryDomain> users;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    private UserSummaryPageDomain(
            final List<UserSummaryDomain> users,
            final int page,
            final int size,
            final long totalElements,
            final int totalPages) {

        this.users = List.copyOf(Objects.requireNonNullElse(users, List.of()));
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 0);
        this.totalElements = Math.max(totalElements, 0L);

        final long inferredTotalPages = totalPages < 0 ? 0 : totalPages;
        this.totalPages = (int) Math.max(inferredTotalPages, this.totalElements > 0 ? 1 : 0);
    }

    public static UserSummaryPageDomain create(
            final List<UserSummaryDomain> users,
            final int page,
            final int size,
            final long totalElements,
            final int totalPages) {

        return new UserSummaryPageDomain(users, page, size, totalElements, totalPages);
    }

    public List<UserSummaryDomain> getUsers() {
        return users;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean hasNext() {
        return totalPages == 0 ? false : page + 1 < totalPages;
    }

    public boolean hasPrevious() {
        return totalPages == 0 ? false : page > 0;
    }
}
