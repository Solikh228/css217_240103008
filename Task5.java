record BrewParameters(double tempCelsius, int extractionSeconds, String grindSize) {}

interface BrewingStrategy {
    BrewParameters getParameters();
}

class EspressoPullStrategy implements BrewingStrategy {
    public BrewParameters getParameters() { return new BrewParameters(93.0, 28, "FINE"); }
}

class PourOverStrategy implements BrewingStrategy {
    public BrewParameters getParameters() { return new BrewParameters(96.0, 180, "MEDIUM"); }
}

class FrenchPressStrategy implements BrewingStrategy {
    public BrewParameters getParameters() { return new BrewParameters(94.0, 240, "COARSE"); }
}

class BeanGrinder {
    String grind(String beanType, String grindSize) { return "Ground(" + beanType + ", " + grindSize + ")"; }
}

class WaterBoiler {
    double heatWater(double targetTemp) { return targetTemp; }
}

class BrewChamber {
    String brew(String grounds, double temp, int seconds) {
        return "Beverage[" + grounds + " @ " + temp + "C for " + seconds + "s]";
    }
}

class BaristaFacade {
    private BrewingStrategy strategy;
    private final BeanGrinder grinder = new BeanGrinder();
    private final WaterBoiler boiler = new WaterBoiler();
    private final BrewChamber chamber = new BrewChamber();

    BaristaFacade(BrewingStrategy strategy) { this.strategy = strategy; }
    void setStrategy(BrewingStrategy strategy) { this.strategy = strategy; }

    String prepareBeverage(String beanType) {
        BrewParameters p = strategy.getParameters();
        String grounds = grinder.grind(beanType, p.grindSize());
        double temp = boiler.heatWater(p.tempCelsius());
        return chamber.brew(grounds, temp, p.extractionSeconds());
    }
}