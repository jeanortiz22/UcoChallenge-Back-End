package co.edu.uco.ucochallenge.user.listusers.application.interactor.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryDomain;
import co.edu.uco.ucochallenge.user.listusers.application.usecase.domain.UserSummaryPageDomain;

class PagedUsersResponseDTOTest {

    @Test
    void constructorShouldSanitizeValues() {
        final PagedUsersResponseDTO dto = new PagedUsersResponseDTO(null, -1, -1, -1, -1, true, true);
        assertThat(dto.users()).isEmpty();
        assertThat(dto.page()).isZero();
        assertThat(dto.size()).isZero();
        assertThat(dto.totalElements()).isZero();
        assertThat(dto.totalPages()).isZero();
        assertThat(dto.hasNext()).isFalse();
        assertThat(dto.hasPrevious()).isFalse();
    }

    @Test
    void fromShouldRequireNonNullDomain() {
        assertThatThrownBy(() -> PagedUsersResponseDTO.from(null, List.of()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void fromShouldCopyPaginationInformation() {
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

        final UserSummaryPageDomain page = UserSummaryPageDomain.create(
                List.of(summary),
                1,
                20,
                40,
                3);

        final PagedUsersResponseDTO dto = PagedUsersResponseDTO.from(page, List.of(new UserResponseDTO(
                summary.getId(),
                summary.getIdType(),
                summary.getIdNumber(),
                summary.getFirstName(),
                summary.getSecondName(),
                summary.getFirstSurname(),
                summary.getSecondSurname(),
                summary.getHomeCity(),
                summary.getEmail(),
                summary.getMobileNumber(),
                summary.isEmailConfirmed(),
                summary.isMobileNumberConfirmed())));

        assertThat(dto.page()).isEqualTo(1);
        assertThat(dto.size()).isEqualTo(20);
        assertThat(dto.totalElements()).isEqualTo(40);
        assertThat(dto.totalPages()).isEqualTo(3);
        assertThat(dto.hasNext()).isTrue();
        assertThat(dto.hasPrevious()).isTrue();
        assertThat(dto.users()).hasSize(1);
    }
}