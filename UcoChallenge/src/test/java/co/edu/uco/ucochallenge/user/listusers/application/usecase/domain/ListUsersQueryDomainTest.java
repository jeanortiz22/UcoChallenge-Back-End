package co.edu.uco.ucochallenge.user.listusers.application.usecase.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ListUsersQueryDomainTest {

    @Test
    void createShouldClampValues() {
        final ListUsersQueryDomain domain = ListUsersQueryDomain.create(-1, 500);
        assertThat(domain.getPage()).isZero();
        assertThat(domain.getSize()).isEqualTo(100);
    }

    @Test
    void fromShouldReturnDefaultWhenNull() {
        final ListUsersQueryDomain domain = ListUsersQueryDomain.from(null);
        assertThat(domain.getPage()).isZero();
        assertThat(domain.getSize()).isEqualTo(5);
    }

    @Test
    void fromShouldCopyValues() {
        final ListUsersQueryDomain original = ListUsersQueryDomain.create(2, 25);
        final ListUsersQueryDomain copy = ListUsersQueryDomain.from(original);

        assertThat(copy.getPage()).isEqualTo(2);
        assertThat(copy.getSize()).isEqualTo(25);
    }
}