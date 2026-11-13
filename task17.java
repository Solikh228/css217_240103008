package task17;

import java.util.*;

class ComplianceScreener {
    private final Set<String> sanctionedAccounts = Set.of("ACC-BLACKLIST");

    public boolean passScreening(String fromAcct, String toAcct) {
        return !sanctionedAccounts.contains(fromAcct) && !sanctionedAccounts.contains(toAcct);
    }
}

class ExchangeRateService {
    public double convert(double amount, String fromCurr, String toCurr) {
        if (fromCurr.equalsIgnoreCase(toCurr)) return amount;
        if (fromCurr.equals("USD") && toCurr.equals("EUR")) return amount * 0.92;
        if (fromCurr.equals("EUR") && toCurr.equals("USD")) return amount * 1.09;
        return amount;
    }
}

class AccountService {
    private final Map<String, Double> balances = new HashMap<>(Map.of(
            "ACC-100", 1000.0,
            "ACC-200", 500.0
    ));
    private final Map<String, String> currencies = Map.of(
            "ACC-100", "USD",
            "ACC-200", "EUR",
            "ACC-FROZEN", "EUR"
    );
    private final Set<String> frozenAccounts = Set.of("ACC-FROZEN");

    public String getCurrency(String acct) {
        return currencies.getOrDefault(acct, "USD");
    }

    public boolean debit(String acct, double amount) {
        if (!balances.containsKey(acct) || frozenAccounts.contains(acct)) return false;
        double current = balances.get(acct);
        if (current < amount) return false;
        balances.put(acct, current - amount);
        System.out.printf("[AccountService] Debited %.2f %s from %s%n", amount, getCurrency(acct), acct);
        return true;
    }

    public boolean credit(String acct, double amount) {
        if (!balances.containsKey(acct) || frozenAccounts.contains(acct)) {
            System.out.println("[AccountService] ERROR: Destination account " + acct + " is invalid or frozen.");
            return false;
        }
        balances.put(acct, balances.get(acct) + amount);
        System.out.printf("[AccountService] Credited %.2f %s to %s%n", amount, getCurrency(acct), acct);
        return true;
    }

    public void refund(String acct, double amount) {
        balances.put(acct, balances.get(acct) + amount);
        System.out.printf("[AccountService] REFUNDED %.2f %s back to %s%n", amount, getCurrency(acct), acct);
    }

    public double getBalance(String acct) {
        return balances.getOrDefault(acct, 0.0);
    }
}

class LedgerService {
    public void record(String status, String from, String to, double amount, String currency) {
        System.out.printf("[Ledger] %s | %s -> %s | %.2f %s%n%n", status, from, to, amount, currency);
    }
}

class WireTransferFacade {
    private final ComplianceScreener compliance;
    private final ExchangeRateService fxService;
    private final AccountService accounts;
    private final LedgerService ledger;

    public WireTransferFacade(ComplianceScreener compliance, ExchangeRateService fxService,
                                AccountService accounts, LedgerService ledger) {
        this.compliance = compliance;
        this.fxService = fxService;
        this.accounts = accounts;
        this.ledger = ledger;
    }

    public boolean executeTransfer(String fromAcct, String toAcct, double amount, String targetCurrency) {
        if (!compliance.passScreening(fromAcct, toAcct)) {
            ledger.record("REJECTED_COMPLIANCE", fromAcct, toAcct, amount, targetCurrency);
            return false;
        }

        if (!accounts.debit(fromAcct, amount)) {
            ledger.record("FAILED_DEBIT", fromAcct, toAcct, amount, targetCurrency);
            return false;
        }

        String sourceCurrency = accounts.getCurrency(fromAcct);
        double convertedAmount = fxService.convert(amount, sourceCurrency, targetCurrency);

        if (!accounts.credit(toAcct, convertedAmount)) {
            
            accounts.refund(fromAcct, amount);
            ledger.record("ABORTED_ROLLED_BACK", fromAcct, toAcct, amount, sourceCurrency);
            return false;
        }

        ledger.record("COMPLETED", fromAcct, toAcct, convertedAmount, targetCurrency);
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        AccountService accounts = new AccountService();
        WireTransferFacade wire = new WireTransferFacade(
                new ComplianceScreener(), new ExchangeRateService(), accounts, new LedgerService()
        );

        
        wire.executeTransfer("ACC-100", "ACC-200", 200.0, "EUR");

        
        wire.executeTransfer("ACC-100", "ACC-FROZEN", 150.0, "EUR");
        System.out.println("ACC-100 final balance: " + accounts.getBalance("ACC-100"));
    }
}