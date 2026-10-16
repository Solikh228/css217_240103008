interface DiscountStrategy {
    double applyDiscount(double price);
}

class FlatDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {

        return Math.max(0.0, price - 50.0);
    }
}

class PercentageDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {
        return price * 0.85; 
    }
}

class SeasonalDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {
        if (price > 500.0) {
            return price * 0.75;
        }
        return price * 0.95;
    }
}

class ShoppingCart {
    private DiscountStrategy strategy;

    public ShoppingCart(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double checkout(double total) {
        if (strategy == null) return total;
        return strategy.applyDiscount(total);
    }
}