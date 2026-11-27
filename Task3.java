record Order(String orderId, double weightKg, double distanceKm) {}

interface ShippingRateStrategy {
    double calculateCost(double weightKg, double distanceKm);
}

class StandardGroundStrategy implements ShippingRateStrategy {
    public double calculateCost(double w, double d) { return 5.0 + w * 0.8 + d * 0.02; }
}

class ExpressAirStrategy implements ShippingRateStrategy {
    public double calculateCost(double w, double d) { return 15.0 + w * 2.5 + d * 0.08; }
}

class SameDayCourierStrategy implements ShippingRateStrategy {
    public double calculateCost(double w, double d) { return 25.0 + w * 3.0 + d * 0.5; }
}

class BoxSelector {
    String selectBox(double weightKg) {
        if (weightKg < 2.0) return "SMALL_BOX";
        if (weightKg < 10.0) return "MEDIUM_BOX";
        return "HEAVY_CRATE";
    }
}

class RateCalculator {
    double finalizeTotal(double baseCost, String boxType) {
        double surcharge = "HEAVY_CRATE".equals(boxType) ? 4.0 : 1.0;
        return Math.round((baseCost + surcharge) * 100.0) / 100.0;
    }
}

class LabelPrinter {
    void printLabel(String orderId, String box, double total) {
        System.out.printf("LABEL [%s] | Box: %s | Cost: $%.2f%n", orderId, box, total);
    }
}

class ShippingFacade {
    private ShippingRateStrategy strategy;
    private final BoxSelector boxSelector = new BoxSelector();
    private final RateCalculator rateCalc = new RateCalculator();
    private final LabelPrinter printer = new LabelPrinter();

    ShippingFacade(ShippingRateStrategy strategy) { this.strategy = strategy; }
    void setStrategy(ShippingRateStrategy strategy) { this.strategy = strategy; }

    double dispatchOrder(Order order) {
        String box = boxSelector.selectBox(order.weightKg());
        double base = strategy.calculateCost(order.weightKg(), order.distanceKm());
        double total = rateCalc.finalizeTotal(base, box);
        printer.printLabel(order.orderId(), box, total);
        return total;
    }
}