package co.edu.uco.ucochallenge.user.listusers.application.usecase.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class UserSummaryPageDomainTest {

    @Test
    void createShouldNormalizeValues() {
        final UserSummaryPageDomain page = UserSummaryPageDomain.create(null, -1, -1, -10, -5);
        assertThat(page.getUsers()).isEmpty();
        assertThat(page.getPage()).isZero();
        assertThat(page.getSize()).isZero();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
        assertThat(page.hasNext()).isFalse();
        assertThat(page.hasPrevious()).isFalse();
    }

    @Test
    void shouldComputeNavigationFlags() {
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

        final UserSummaryPageDomain page = UserSummaryPageDomain.create(List.of(summary), 1, 20, 40, 3);

        assertThat(page.hasNext()).isTrue();
        assertThat(page.hasPrevious()).isTrue();
    }
}
