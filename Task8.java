import java.util.List;

record CartItem(String sku, int qty, double unitPrice) {}

interface PaymentStrategy {
    String authorizeAndCapture(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private final String token;
    CreditCardPayment(String token) { this.token = token; }
    public String authorizeAndCapture(double amount) {
        if (token.startsWith("INVALID")) throw new IllegalStateException("Card declined");
        return "CC_" + Math.round(amount * 100);
    }
}

class DigitalWalletPayment implements PaymentStrategy {
    private final String walletId;
    DigitalWalletPayment(String walletId) { this.walletId = walletId; }
    public String authorizeAndCapture(double amount) {
        return "WALLET_" + walletId + "_" + Math.round(amount);
    }
}

class BankTransferPayment implements PaymentStrategy {
    private final String iban;
    BankTransferPayment(String iban) { this.iban = iban; }
    public String authorizeAndCapture(double amount) {
        return "IBAN_" + iban.substring(Math.max(0, iban.length() - 4));
    }
}

class InventoryLockService {
    String lock(List<CartItem> items) { return "LOCK_01"; }
    void release(String lockId) { System.out.println("Released lock " + lockId); }
}

class TaxEngine {
    double computeTax(double subtotal) { return Math.round(subtotal * 0.12 * 100.0) / 100.0; }
}

class PaymentProcessor {
    String execute(PaymentStrategy s, double total) { return s.authorizeAndCapture(total); }
}

class ReceiptGenerator {
    String generate(String txnId, double total) { return "RECEIPT[" + txnId + "] $" + total; }
}

class CheckoutFacade {
    private PaymentStrategy strategy;
    private final InventoryLockService inventory = new InventoryLockService();
    private final TaxEngine tax = new TaxEngine();
    private final PaymentProcessor processor = new PaymentProcessor();
    private final ReceiptGenerator receipts = new ReceiptGenerator();

    CheckoutFacade(PaymentStrategy defaultStrategy) { this.strategy = defaultStrategy; }
    void setPaymentStrategy(PaymentStrategy strategy) { this.strategy = strategy; }

    String checkout(List<CartItem> items, PaymentStrategy overrideStrategy) {
        PaymentStrategy active = overrideStrategy != null ? overrideStrategy : strategy;
        String lockId = inventory.lock(items);
        try {
            double sub = items.stream().mapToDouble(i -> i.qty() * i.unitPrice()).sum();
            double total = sub + tax.computeTax(sub);
            String txn = processor.execute(active, total);
            return receipts.generate(txn, total);
        } catch (RuntimeException ex) {
            inventory.release(lockId);
            throw ex;
        }
    }
}