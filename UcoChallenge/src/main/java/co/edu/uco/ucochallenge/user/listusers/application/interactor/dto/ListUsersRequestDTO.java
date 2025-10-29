package co.edu.uco.ucochallenge.user.listusers.application.interactor.dto;

public record ListUsersRequestDTO(int page, int size) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 100;

    public ListUsersRequestDTO {
        page = page < DEFAULT_PAGE ? DEFAULT_PAGE : page;

        if (size < MIN_SIZE) {
            size = DEFAULT_SIZE;
        } else if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
    }

    public static ListUsersRequestDTO normalize(final Integer page, final Integer size) {
        final int sanitizedPage = page == null ? DEFAULT_PAGE : page;
        final int sanitizedSize = size == null ? DEFAULT_SIZE : size;
        return new ListUsersRequestDTO(sanitizedPage, sanitizedSize);
    }

    public static ListUsersRequestDTO normalize(final ListUsersRequestDTO dto) {
        if (dto == null) {
            return new ListUsersRequestDTO(DEFAULT_PAGE, DEFAULT_SIZE);
        }
        return new ListUsersRequestDTO(dto.page(), dto.size());
    }
}