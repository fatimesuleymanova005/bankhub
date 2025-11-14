package bankhub;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements Identifiable {
    private final UUID id;
    private final UUID accountId;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime date;

    public Transaction(UUID accountId, TransactionType type, double amount) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "%s %s: %.2f$ at %s".formatted(accountId, type, amount, date);
    }
}


