import java.util.List;

record LineItem(String sku, double price, int qty) {}

interface DiscountStrategy {
    double calculateDiscount(double subtotal, int itemCount, String loyaltyTier);
}

class SeasonalPercentageDiscount implements DiscountStrategy {
    private final double rate;
    SeasonalPercentageDiscount(double rate) { this.rate = rate; }
    public double calculateDiscount(double subtotal, int itemCount, String tier) {
        return Math.round(subtotal * rate * 100.0) / 100.0;
    }
}

class TieredLoyaltyDiscount implements DiscountStrategy {
    public double calculateDiscount(double subtotal, int itemCount, String tier) {
        double rate = switch (tier) {
            case "GOLD" -> 0.20;
            case "SILVER" -> 0.10;
            case "BRONZE" -> 0.05;
            default -> 0.0;
        };
        return Math.round(subtotal * rate * 100.0) / 100.0;
    }
}

class BundleThresholdDiscount implements DiscountStrategy {
    public double calculateDiscount(double subtotal, int itemCount, String tier) {
        return (itemCount >= 5 && subtotal >= 100.0) ? 25.0 : 0.0;
    }
}

class CartAnalyzer {
    double subtotal(List<LineItem> cart) { return cart.stream().mapToDouble(i -> i.price() * i.qty()).sum(); }
    int count(List<LineItem> cart) { return cart.stream().mapToInt(LineItem::qty).sum(); }
}

class MemberProfileService {
    String getTier(String userId) { return "GOLD"; }
}

class CouponValidator {
    boolean isEligible(String userId, double subtotal) { return subtotal > 0; }
}

class PointsLedger {
    void creditPoints(String userId, double paid) {
        System.out.println("Credited " + (int) paid + " pts to " + userId);
    }
}

class BillingFacade {
    private DiscountStrategy strategy;
    private final CartAnalyzer analyzer = new CartAnalyzer();
    private final MemberProfileService profiles = new MemberProfileService();
    private final CouponValidator validator = new CouponValidator();
    private final PointsLedger ledger = new PointsLedger();

    BillingFacade(DiscountStrategy strategy) { this.strategy = strategy; }
    void setStrategy(DiscountStrategy strategy) { this.strategy = strategy; }

    double finalizeCart(String userId, List<LineItem> cart) {
        double sub = analyzer.subtotal(cart);
        int cnt = analyzer.count(cart);
        String tier = profiles.getTier(userId);
        double disc = validator.isEligible(userId, sub) ? strategy.calculateDiscount(sub, cnt, tier) : 0.0;
        double finalAmt = Math.max(0.0, Math.round((sub - disc) * 100.0) / 100.0);
        ledger.creditPoints(userId, finalAmt);
        return finalAmt;
    }
}