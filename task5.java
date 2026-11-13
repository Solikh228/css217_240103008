package task05;

class InventoryService {
    public void reserveStock(String itemId) {
        System.out.println("[Inventory] Reserved item: " + itemId);
    }
}

class PaymentProcessor {
    public void charge(double amount) {
        System.out.printf("[Payment] Charged $%.2f from customer card.%n", amount);
    }
}

class ShippingService {
    public void createLabel(String itemId, String address) {
        System.out.println("[Shipping] Generated label for " + itemId + " -> " + address);
    }
}

class OrderFacade {
    private final InventoryService inventory;
    private final PaymentProcessor payment;
    private final ShippingService shipping;

    public OrderFacade(InventoryService inventory, PaymentProcessor payment, ShippingService shipping) {
        this.inventory = inventory;
        this.payment = payment;
        this.shipping = shipping;
    }

    public void placeOrder(String itemId, double amount, String address) {
        System.out.println("=== Processing Order (" + itemId + ") ===");
        inventory.reserveStock(itemId);
        payment.charge(amount);
        shipping.createLabel(itemId, address);
        System.out.println("Order completed!\n");
    }
}

public class Main {
    public static void main(String[] args) {
        OrderFacade store = new OrderFacade(
                new InventoryService(), new PaymentProcessor(), new ShippingService()
        );
        store.placeOrder("LAPTOP-99", 1299.50, "221B Baker Street");
        store.placeOrder("MOUSE-04", 45.00, "742 Evergreen Terrace");
    }
}