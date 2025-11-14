package bankhub;

@FunctionalInterface
public interface Formatter<T> {
    String format(T entity);
}
