package bankhub;

import java.util.UUID;

public class User implements Identifiable {
    private final UUID id;
    private final String name;
    private final String email;

    public User(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "%s <%s>".formatted(name, email);
    }
}
