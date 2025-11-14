package bankhub;

import exceptions.*;

import java.util.*;

public class BankService {
    private final Repository<Account> accountRepo;
    private final Repository<Transaction> transactionRepo;
    private final Set<String> owners = new HashSet<>();

    public BankService(Repository<Account> accountRepo, Repository<Transaction> transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public Account createAccount(String ownerName, AccountType type, double balance) throws DuplicateAccountException {
        if (owners.contains(ownerName + type)) {
            throw new DuplicateAccountException("Account with owner " + ownerName + " and type " + type + " already exists.");
        }
        Account acc = new Account(ownerName, type, balance);
        accountRepo.save(acc);
        owners.add(ownerName + type);
        return acc;
    }

    public void deposit(UUID accountId, double amount) throws InvalidAmountException, AccountNotFoundException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be > 0");
        Account acc = accountRepo.findById(accountId);
        if (acc == null) throw new AccountNotFoundException("Account not found");
        acc.deposit(amount);
        transactionRepo.save(new Transaction(accountId, TransactionType.DEPOSIT, amount));
    }

    public void withdraw(UUID accountId, double amount) throws InvalidAmountException, InsufficientFundsException, AccountNotFoundException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be > 0");
        Account acc = accountRepo.findById(accountId);
        if (acc == null) throw new AccountNotFoundException("Account not found");
        if (acc.getBalance() < amount) throw new InsufficientFundsException("Insufficient funds");
        acc.withdraw(amount);
        transactionRepo.save(new Transaction(accountId, TransactionType.WITHDRAW, amount));
    }

    public void transfer(UUID fromId, UUID toId, double amount) throws TransferFailedException, InvalidAmountException, InsufficientFundsException, AccountNotFoundException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be > 0");
        Account from = accountRepo.findById(fromId);
        Account to = accountRepo.findById(toId);
        if (from == null || to == null) throw new TransferFailedException("Transfer failed: account not found");
        if (from.getBalance() < amount) throw new InsufficientFundsException("Insufficient funds");
        from.withdraw(amount);
        to.deposit(amount);
        transactionRepo.save(new Transaction(fromId, TransactionType.TRANSFER, amount));
        transactionRepo.save(new Transaction(toId, TransactionType.TRANSFER, amount));
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
}
}