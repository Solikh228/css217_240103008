record LightingConfig(int brightness, int colorTemp) {}

interface LightingStrategy {
    LightingConfig calculate(double ambientLux);
}

class EcoDimStrategy implements LightingStrategy {
    public LightingConfig calculate(double ambientLux) {
        int brightness = Math.max(10, (int) (50 - ambientLux * 0.05));
        return new LightingConfig(brightness, 3000);
    }
}

class ReadingFocusStrategy implements LightingStrategy {
    public LightingConfig calculate(double ambientLux) {
        return new LightingConfig(95, 5000);
    }
}

class AmbientPartyStrategy implements LightingStrategy {
    public LightingConfig calculate(double ambientLux) {
        return new LightingConfig(75, 2200);
    }
}

class PowerGridMonitor {
    boolean isGridStable() { return true; }
}

class DimmerHardware {
    void setOutput(String roomId, LightingConfig cfg) {
        System.out.println("[" + roomId + "] Brightness=" + cfg.brightness() + "%, Temp=" + cfg.colorTemp() + "K");
    }
}

class RoomSensor {
    double readAmbientLux(String roomId) { return 200.0; }
}

class HomeLightingFacade {
    private LightingStrategy strategy;
    private final PowerGridMonitor grid = new PowerGridMonitor();
    private final DimmerHardware dimmer = new DimmerHardware();
    private final RoomSensor sensor = new RoomSensor();

    HomeLightingFacade(LightingStrategy strategy) { this.strategy = strategy; }
    void setStrategy(LightingStrategy strategy) { this.strategy = strategy; }

    void applyScene(String roomId) {
        if (!grid.isGridStable()) throw new IllegalStateException("Grid unstable");
        double lux = sensor.readAmbientLux(roomId);
        dimmer.setOutput(roomId, strategy.calculate(lux));
    }
}