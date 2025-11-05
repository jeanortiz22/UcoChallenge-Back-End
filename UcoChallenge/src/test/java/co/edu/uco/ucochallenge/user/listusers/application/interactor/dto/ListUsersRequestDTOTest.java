package co.edu.uco.ucochallenge.user.listusers.application.interactor.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ListUsersRequestDTOTest {

    @Test
    void normalizeShouldApplyDefaultsWhenValuesAreNull() {
        final ListUsersRequestDTO normalized = ListUsersRequestDTO.normalize(null, null);
        assertThat(normalized.page()).isZero();
        assertThat(normalized.size()).isEqualTo(20);
    }

    @Test
    void normalizeShouldClampValuesWithinBounds() {
        final ListUsersRequestDTO normalized = ListUsersRequestDTO.normalize(-1, 500);
        assertThat(normalized.page()).isZero();
        assertThat(normalized.size()).isEqualTo(100);
    }

    @Test
    void normalizeShouldHandleNullRecord() {
        final ListUsersRequestDTO normalized = ListUsersRequestDTO.normalize((ListUsersRequestDTO) null);
        assertThat(normalized.page()).isZero();
        assertThat(normalized.size()).isEqualTo(20);
    }

    @Test
    void constructorShouldApplyConstraints() {
        final ListUsersRequestDTO dto = new ListUsersRequestDTO(-5, 0);
        assertThat(dto.page()).isZero();
        assertThat(dto.size()).isEqualTo(20);
    }
}