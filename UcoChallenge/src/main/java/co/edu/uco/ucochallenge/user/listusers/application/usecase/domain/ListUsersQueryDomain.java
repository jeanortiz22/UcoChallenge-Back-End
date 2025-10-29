package co.edu.uco.ucochallenge.user.listusers.application.usecase.domain;

import java.util.Objects;

public final class ListUsersQueryDomain {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 100;

    private final int page;
    private final int size;

    private ListUsersQueryDomain(final int page, final int size) {
        this.page = page < DEFAULT_PAGE ? DEFAULT_PAGE : page;

        if (size < MIN_SIZE) {
            this.size = DEFAULT_SIZE;
        } else if (size > MAX_SIZE) {
            this.size = MAX_SIZE;
        } else {
            this.size = size;
        }
    }

    public static ListUsersQueryDomain create(final int page, final int size) {
        return new ListUsersQueryDomain(page, size);
    }

    public static ListUsersQueryDomain from(final ListUsersQueryDomain query) {
        return Objects.isNull(query)
                ? new ListUsersQueryDomain(DEFAULT_PAGE, DEFAULT_SIZE)
                : new ListUsersQueryDomain(query.getPage(), query.getSize());
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}