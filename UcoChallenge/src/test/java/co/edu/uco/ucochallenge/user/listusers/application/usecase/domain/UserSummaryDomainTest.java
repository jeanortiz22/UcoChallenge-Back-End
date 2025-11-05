package co.edu.uco.ucochallenge.user.listusers.application.usecase.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserSummaryDomainTest {

    @Test
    void createShouldSanitizeValues() {
        final UserSummaryDomain domain = UserSummaryDomain.create(
                null,
                null,
                " 123 ",
                " John ",
                null,
                " Doe ",
                null,
                null,
                " user@example.com ",
                " +573001234567 ",
                true,
                false);

        assertThat(domain.getId()).isEqualTo(new UUID(0L, 0L));
        assertThat(domain.getIdType()).isEqualTo(new UUID(0L, 0L));
        assertThat(domain.getIdNumber()).isEqualTo("123");
        assertThat(domain.getFirstName()).isEqualTo("John");
        assertThat(domain.getSecondName()).isEmpty();
        assertThat(domain.getFirstSurname()).isEqualTo("Doe");
        assertThat(domain.getSecondSurname()).isEmpty();
        assertThat(domain.getHomeCity()).isEqualTo(new UUID(0L, 0L));
        assertThat(domain.getEmail()).isEqualTo("user@example.com");
        assertThat(domain.getMobileNumber()).isEqualTo("+573001234567");
        assertThat(domain.isEmailConfirmed()).isTrue();
        assertThat(domain.isMobileNumberConfirmed()).isFalse();
    }
}