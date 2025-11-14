package bankhub;

import java.util.*;

public class InMemoryRepository<T extends Identifiable> implements Repository<T> {
    private final Map<UUID, T> store = new HashMap<>();

    @Override
    public void save(T entity) {
        store.put(entity.getId(), entity);
    }

    @Override
    public T findById(UUID id) {
        return store.get(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(UUID id) {
        store.remove(id);
    }
}
