class FraudService { boolean check(double amt) { return amt <= 1000; } }
class PaymentService { void pay(double amt) { System.out.println("Paid: " + amt); } }
class ShippingService { void ship(String item) { System.out.println("Shipped: " + item); } }

class OrderFacade {
    private final FraudService fraud = new FraudService();
    private final PaymentService payment = new PaymentService();
    private final ShippingService shipping = new ShippingService();

    public boolean placeOrder(String item, double amount) {
        if (!fraud.check(amount)) {
            System.out.println("Fraud detected! Order cancelled: " + amount);
            return false;
        }
        payment.pay(amount);
        shipping.ship(item);
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        OrderFacade order = new OrderFacade();
        order.placeOrder("Phone", 500);
        order.placeOrder("Laptop", 5000); 
    }
}