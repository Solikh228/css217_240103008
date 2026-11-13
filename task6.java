package task06;

class WaterFilter {
    public void cycle() { System.out.println("[Filter] Running water filtration cycle."); }
}

class AquariumHeater {
    public void regulate(double temp) {
        System.out.println("[Heater] Maintaining water at " + temp + "°C.");
    }
}

class AutoFeeder {
    public void feed() { System.out.println("[AutoFeeder] Dispensed a pinch of flakes."); }
}

class LedHoodLight {
    public void setDaylight() { System.out.println("[LED] Switched to bright daylight mode."); }
    public void setMoonlight() { System.out.println("[LED] Switched to soft blue moonlight."); }
}

class AquariumFacade {
    private final WaterFilter filter;
    private final AquariumHeater heater;
    private final AutoFeeder feeder;
    private final LedHoodLight light;

    public AquariumFacade(WaterFilter filter, AquariumHeater heater, AutoFeeder feeder, LedHoodLight light) {
        this.filter = filter;
        this.heater = heater;
        this.feeder = feeder;
        this.light = light;
    }

    public void runMorningRoutine() {
        System.out.println("=== Aquarium Morning Routine ===");
        light.setDaylight();
        heater.regulate(25.5);
        filter.cycle();
        feeder.feed();
    }

    public void prepareForNight() {
        System.out.println("\n=== Aquarium Night Setup ===");
        light.setMoonlight();
        heater.regulate(24.0);
        filter.cycle();
    }
}

public class Main {
    public static void main(String[] args) {
        AquariumFacade aquarium = new AquariumFacade(
                new WaterFilter(), new AquariumHeater(), new AutoFeeder(), new LedHoodLight()
        );
        aquarium.runMorningRoutine();
        aquarium.prepareForNight();
    }
}