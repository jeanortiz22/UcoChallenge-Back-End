package co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.specification;

@FunctionalInterface
public interface Specification<T> {

    T apply(T candidate);

    default Specification<T> and(final Specification<T> other) {
        return candidate -> other.apply(apply(candidate));
    }

    static <T> Specification<T> identity() {
        return candidate -> candidate;
    }
}
