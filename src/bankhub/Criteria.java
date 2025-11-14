package bankhub;

@FunctionalInterface
public interface Criteria<T> {
    boolean test(T entity);
}
