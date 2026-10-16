interface ShippingStrategy {
    double calculate(double weightKg, double distanceKm);
}

class StandardShipping implements ShippingStrategy {
    
    public double calculate(double weightKg, double distanceKm) {
        return (weightKg * 2.0) + (distanceKm * 0.5);
    }
}

class ExpressShipping implements ShippingStrategy {
    public double calculate(double weightKg, double distanceKm) {
        return (weightKg * 5.0) + (distanceKm * 1.2) + 50.0;
    }
}

class DroneShipping implements ShippingStrategy {
    public double calculate(double weightKg, double distanceKm) {
        if (weightKg < 5.0) {
            return 100.0;
        } else {
            return -1.0; 
        }
    }
}