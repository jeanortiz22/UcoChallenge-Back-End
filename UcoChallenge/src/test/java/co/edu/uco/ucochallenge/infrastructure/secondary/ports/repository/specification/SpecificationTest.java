package co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SpecificationTest {

    @Test
    void identityShouldReturnSameInstance() {
        final var candidate = new Object();
        assertThat(Specification.identity().apply(candidate)).isSameAs(candidate);
    }

    @Test
    void andShouldComposeSpecifications() {
        final Specification<StringBuilder> appendHello = builder -> {
            builder.append("Hello");
            return builder;
        };

        final Specification<StringBuilder> appendWorld = builder -> {
            builder.append(" World");
            return builder;
        };

        final Specification<StringBuilder> composed = appendHello.and(appendWorld);

        final StringBuilder result = composed.apply(new StringBuilder());

        assertThat(result.toString()).isEqualTo("Hello World");
    }
}