package bankhub;

import exceptions.DuplicateAccountException;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Repository<Account> accRepo = new InMemoryRepository<>();
        Repository<Transaction> txRepo = new InMemoryRepository<>();
        BankService svc = new BankService(accRepo, txRepo);

        // Optional: load sample accounts


        while (true) {
            System.out.println("""
                \n=== BankHub ===
                1) Create account
                2) Deposit
                3) Withdraw
                4) Transfer
                5) List accounts (sorted)
                6) Filter accounts (balance > 1000)
                7) Show owners (unique, sorted)
                8) Show transactions grouped by account
                9) Trigger common errors (demo)
                0) Exit
                """);
            System.out.print("Choose: ");
            switch (in.nextLine().trim()) {
                case "1" -> createAccountFlow(svc);
                case "2" -> depositFlow(svc);
                case "3" -> withdrawFlow(svc);
                case "4" -> transferFlow(svc);
                case "5" -> listSortedFlow(svc);
                case "6" -> filterFlow(svc);
                case "7" -> ownersFlow(svc);
                case "8" -> groupedFlow(svc);
                case "9" -> demoErrors(svc);
                case "0" -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Unknown.");
            }
        }
    }

    // 1) Create account
    private static void createAccountFlow(BankService svc) {
        try {
            System.out.print("Owner name: ");
            String owner = in.nextLine().trim();
            System.out.print("Account type (CHECKING/SAVINGS/BUSINESS): ");
            AccountType type = AccountType.valueOf(in.nextLine().trim().toUpperCase());
            System.out.print("Initial balance: ");
            double balance = Double.parseDouble(in.nextLine().trim());
            Account acc = svc.createAccount(owner, type, balance);
            System.out.println("Account created: " + acc);
        } catch (DuplicateAccountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid account type.");
        }
    }

    // 2) Deposit
    private static void depositFlow(BankService svc) {
        try {
            System.out.print("Account ID: ");
            UUID id = UUID.fromString(in.nextLine().trim());
            System.out.print("Deposit amount: ");
            double amount = Double.parseDouble(in.nextLine().trim());
            svc.deposit(id, amount);
            System.out.println("Deposit successful.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 3) Withdraw
    private static void withdrawFlow(BankService svc) {
        try {
            System.out.print("Account ID: ");
            UUID id = UUID.fromString(in.nextLine().trim());
            System.out.print("Withdraw amount: ");
            double amount = Double.parseDouble(in.nextLine().trim());
            svc.withdraw(id, amount);
            System.out.println("Withdrawal successful.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 4) Transfer
    private static void transferFlow(BankService svc) {
        try {
            System.out.print("From Account ID: ");
            UUID fromId = UUID.fromString(in.nextLine().trim());
            System.out.print("To Account ID: ");
            UUID toId = UUID.fromString(in.nextLine().trim());
            System.out.print("Amount: ");
            double amount = Double.parseDouble(in.nextLine().trim());
            svc.transfer(fromId, toId, amount);
            System.out.println("Transfer successful.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // 5) List accounts sorted by balance
    private static void listSortedFlow(BankService svc) {
        List<Account> accounts = svc.getAllAccounts();
        accounts.sort(Comparator.comparingDouble(Account::getBalance));
        System.out.println("--- Accounts sorted by balance ---");
        accounts.forEach(System.out::println);
    }

    // 6) Filter accounts balance > 1000
    private static void filterFlow(BankService svc) {
        List<Account> accounts = svc.getAllAccounts();
        System.out.println("--- Accounts with balance > 1000 ---");
        accounts.stream()
                .filter(a -> a.getBalance() > 1000)
                .forEach(System.out::println);
    }

    // 7) Show unique owners sorted
    private static void ownersFlow(BankService svc) {
        Set<String> owners = svc.getAllAccounts().stream()
                .map(Account::getOwnerName)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("--- Unique owners ---");
        owners.forEach(System.out::println);
    }

    // 8) Show transactions grouped by account
    private static void groupedFlow(BankService svc) {
        Map<UUID, List<Transaction>> map = svc.getAllTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId));
        System.out.println("--- Transactions grouped by account ---");
        map.forEach((id, txs) -> {
            System.out.println("Account ID: " + id);
            txs.forEach(System.out::println);
        });
    }

    // 9) Demo common errors
    private static void demoErrors(BankService svc) {
        try {
            System.out.println("--- Demo: deposit negative amount ---");
            Account acc = svc.getAllAccounts().get(0);
            svc.deposit(acc.getId(), -50);
        } catch (Exception e) {
            System.out.println("Caught error: " + e.getMessage());
        }

        try {
            System.out.println("--- Demo: withdraw too much ---");
            Account acc = svc.getAllAccounts().get(0);
            svc.withdraw(acc.getId(), acc.getBalance() + 5000);
        } catch (Exception e) {
            System.out.println("Caught error: " + e.getMessage());
        }

        try {
            System.out.println("--- Demo: transfer to non-existing account ---");
            Account acc = svc.getAllAccounts().get(0);
            svc.transfer(acc.getId(), UUID.randomUUID(), 100);
        } catch (Exception e) {
            System.out.println("Caught error: " + e.getMessage());
        }

        try {
            System.out.println("--- Demo: duplicate account ---");
            Account acc = svc.getAllAccounts().get(0);
            svc.createAccount(acc.getOwnerName(), acc.getType(), 100);
        } catch (Exception e) {
            System.out.println("Caught error: " + e.getMessage());
 }
}
}