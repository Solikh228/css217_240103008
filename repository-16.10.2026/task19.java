interface TaxStrategy {
    double calculateTax(double income);
}

class ZeroTax implements TaxStrategy {
    public double calculateTax(double income) {
        return 0.0;
    }
}

class FlatTax implements TaxStrategy {
    public double calculateTax(double income) {
        return income * 0.10;
    }
}

class ProgressiveTax implements TaxStrategy {
    public double calculateTax(double income) {
        if (income <= 100000.0) {
            return income * 0.05;
        } else {
            return (100000.0 * 0.05) + ((income - 100000.0) * 0.20);
        }
    }
}