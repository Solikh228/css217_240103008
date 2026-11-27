import java.util.List;

record Waypoint(double lat, double lon, double alt) {}

interface FlightPathStrategy {
    List<Waypoint> planTrajectory(List<Waypoint> waypoints);
}

class EnergyOptimalFlight implements FlightPathStrategy {
    public List<Waypoint> planTrajectory(List<Waypoint> wps) {
        return wps.stream().map(w -> new Waypoint(w.lat(), w.lon(), Math.min(w.alt(), 40.0))).toList();
    }
}

class WindCompensationFlight implements FlightPathStrategy {
    public List<Waypoint> planTrajectory(List<Waypoint> wps) {
        return wps.stream().map(w -> new Waypoint(w.lat() + 0.0002, w.lon() - 0.0002, w.alt())).toList();
    }
}

class HighObstacleClearanceFlight implements FlightPathStrategy {
    public List<Waypoint> planTrajectory(List<Waypoint> wps) {
        return wps.stream().map(w -> new Waypoint(w.lat(), w.lon(), Math.max(w.alt(), 120.0))).toList();
    }
}

class GpsBarometerUnit {
    Waypoint currentCoords() { return new Waypoint(43.25, 76.92, 50.0); }
}

class BatteryHealthMonitor {
    private final double voltage;
    BatteryHealthMonitor(double voltage) { this.voltage = voltage; }
    boolean isCritical() { return voltage < 10.2; }
}

class PayloadLatchController {
    boolean isLocked() { return true; }
}

class RotorEscDriver {
    String followPath(List<Waypoint> path) { return "FLIGHT_OK(" + path.size() + " wps)"; }
    String emergencyLand(Waypoint pos) { return "EMERGENCY_LAND(" + pos.lat() + "," + pos.lon() + ")"; }
}

class FlightControlFacade {
    private FlightPathStrategy strategy;
    private final GpsBarometerUnit gps = new GpsBarometerUnit();
    private final BatteryHealthMonitor battery;
    private final PayloadLatchController latch = new PayloadLatchController();
    private final RotorEscDriver esc = new RotorEscDriver();

    FlightControlFacade(FlightPathStrategy strategy, double voltage) {
        this.strategy = strategy;
        this.battery = new BatteryHealthMonitor(voltage);
    }

    void setStrategy(FlightPathStrategy strategy) { this.strategy = strategy; }

    String executeDeliveryLeg(List<Waypoint> waypoints) {
        if (!latch.isLocked()) throw new IllegalStateException("Latch unlocked");
        if (battery.isCritical()) return esc.emergencyLand(gps.currentCoords());
        return esc.followPath(strategy.planTrajectory(waypoints));
    }
}
vibe code