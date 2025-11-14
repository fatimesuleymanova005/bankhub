package bankhub;

public class Seed {
    public static void loadSample(BankService svc) {
        try {
            svc.createAccount("Arzu", AccountType.CHECKING, 1200);
            svc.createAccount("Leyla", AccountType.SAVINGS, 800);
            svc.createAccount("Kamal", AccountType.BUSINESS, 2500);
            svc.createAccount("Nigar", AccountType.SAVINGS, 1700);
            svc.createAccount("Murad", AccountType.CHECKING, 950);
        } catch (Exception ignored) {
        }
    }
}
