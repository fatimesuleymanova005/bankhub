package bankhub;

import java.util.List;
import java.util.UUID;

public interface Repository<T extends Identifiable> {
    void save(T entity);
    T findById(UUID id);
    List<T> findAll();
    void delete(UUID id);
}
