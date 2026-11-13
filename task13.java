package task13;

import java.util.HashMap;
import java.util.Map;

class CardReader {
    public void readCard(String cardNumber) {
        System.out.println("[CardReader] Card inserted: " + cardNumber);
    }
}

class PinAuthenticator {
    private final Map<String, Integer> pins = Map.of(
            "4400-1111", 1234,
            "4400-2222", 9876
    );

    public boolean verify(String cardNumber, int pin) {
        return pins.containsKey(cardNumber) && pins.get(cardNumber) == pin;
    }
}

class AccountLedger {
    private final Map<String, Double> balances = new HashMap<>(Map.of(
            "4400-1111", 500.0,
            "4400-2222", 120.0
    ));

    public double getBalance(String cardNumber) {
        return balances.getOrDefault(cardNumber, 0.0);
    }

    public void debit(String cardNumber, double amount) {
        balances.put(cardNumber, getBalance(cardNumber) - amount);
    }
}

class CashDispenser {
    private double atmCashReserve = 1000.0;

    public boolean hasEnoughCash(double amount) {
        return atmCashReserve >= amount;
    }

    public void dispense(double amount) {
        atmCashReserve -= amount; 
        System.out.printf("[CashDispenser] Dispensed $%.2f.%n", amount);
    }
}

class AuditLogger {
    public void log(String cardNumber, double amount, boolean success, String reason) {
        String status = success ? "SUCCESS" : "REJECTED (" + reason + ")";
        System.out.printf("[AuditLog] Card %s | Amount: $%.2f | Status: %s%n%n", cardNumber, amount, status);
    }
}

class AtmFacade {
    private final CardReader reader;
    private final PinAuthenticator auth;
    private final AccountLedger ledger;
    private final CashDispenser dispenser;
    private final AuditLogger logger;

    public AtmFacade(CardReader reader, PinAuthenticator auth, AccountLedger ledger, CashDispenser dispenser, AuditLogger logger) {
        this.reader = reader;
        this.auth = auth;
        this.ledger = ledger;
        this.dispenser = dispenser;
        this.logger = logger;
    }

    public boolean withdrawCash(String cardNumber, int pin, double amount) {
        reader.readCard(cardNumber);

        if (!auth.verify(cardNumber, pin)) {
            logger.log(cardNumber, amount, false, "Invalid PIN");
            return false;
        }
        if (ledger.getBalance(cardNumber) < amount) {
            logger.log(cardNumber, amount, false, "Insufficient account balance");
            return false;
        }
        if (!dispenser.hasEnoughCash(amount)) {
            logger.log(cardNumber, amount, false, "ATM out of cash");
            return false;
        }

        ledger.debit(cardNumber, amount);
        dispenser.dispense(amount);
        logger.log(cardNumber, amount, true, "OK");
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        AtmFacade atm = new AtmFacade(
                new CardReader(), new PinAuthenticator(), new AccountLedger(), new CashDispenser(), new AuditLogger()
        );

        atm.withdrawCash("4400-1111", 9999, 100.0);
        atm.withdrawCash("4400-2222", 9876, 200.0); 
        atm.withdrawCash("4400-1111", 1234, 150.0); 
    }
}