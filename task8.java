package task08;

class Battery {
    public void checkVoltage() { System.out.println("[Battery] Voltage nominal (12.6V)."); }
}

class FuelPump {
    public void prime() { System.out.println("[FuelPump] Fuel line pressurized."); }
    public void stop() { System.out.println("[FuelPump] Fuel delivery stopped."); }
}

class StarterMotor {
    public void crank() { System.out.println("[Starter] Cranking engine... Vroom!"); }
}

class DashboardDisplay {
    public void illuminate() { System.out.println("[Dashboard] Gauges lit up. Ready to drive."); }
    public void turnOff() { System.out.println("[Dashboard] Displays powered down."); }
}

class CarIgnitionFacade {
    private final Battery battery;
    private final FuelPump fuelPump;
    private final StarterMotor starter;
    private final DashboardDisplay dashboard;

    public CarIgnitionFacade(Battery battery, FuelPump fuelPump, StarterMotor starter, DashboardDisplay dashboard) {
        this.battery = battery;
        this.fuelPump = fuelPump;
        this.starter = starter;
        this.dashboard = dashboard;
    }

    public void startEngine() {
        System.out.println("=== Starting Car ===");
        battery.checkVoltage();
        fuelPump.prime();
        starter.crank();
        dashboard.illuminate();
    }

    public void stopEngine() {
        System.out.println("\n=== Stopping Car ===");
        fuelPump.stop();
        dashboard.turnOff();
        System.out.println("Engine off.");
    }
}

public class Main {
    public static void main(String[] args) {
        CarIgnitionFacade car = new CarIgnitionFacade(
                new Battery(), new FuelPump(), new StarterMotor(), new DashboardDisplay()
        );
        car.startEngine();
        System.out.println("...Driving to campus...");
        car.stopEngine();
    }
}