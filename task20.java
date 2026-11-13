package task20;

record EnergySummary(double solarUsed, double batteryUsed, double gridUsed, double unmetDeficit, boolean loadShedTriggered) {}

class SolarPanelArray {
    private double availableKw;

    public SolarPanelArray(double availableKw) {
        this.availableKw = availableKw;
    }

    public double drawPower(double requestedKw) {
        double provided = Math.min(availableKw, requestedKw);
        availableKw -= provided;
        return provided;
    }
}

class BatteryBank {
    private final double capacityKwh = 100.0;
    private double currentKwh;

    public BatteryBank(double initialKwh) {
        this.currentKwh = initialKwh;
    }

    public double discharge(double requestedKw) {
        double minReserve = capacityKwh * 0.20; 
        double usable = Math.max(0.0, currentKwh - minReserve);
        double provided = Math.min(usable, requestedKw);
        currentKwh -= provided;
        return provided;
    }
}

class ExternalGrid {
    private final double maxImportCapKw;

    public ExternalGrid(double maxImportCapKw) {
        this.maxImportCapKw = maxImportCapKw;
    }

    public double importPower(double requestedKw) {
        return Math.min(maxImportCapKw, requestedKw);
    }
}

class LoadController {
    public void shedNonEssentialCircuits(double deficitKw) {
        System.out.printf("[LoadController] ALERT: Shedding non-essential circuits to cover %.2f kW deficit!%n", deficitKw);
    }
}

class TelemetryLogger {
    public void logEvent(String msg) {
        System.out.println("[Telemetry] " + msg);
    }
}

class MicrogridFacade {
    private final SolarPanelArray solar;
    private final BatteryBank battery;
    private final ExternalGrid grid;
    private final LoadController loadController;
    private final TelemetryLogger telemetry;

    public MicrogridFacade(SolarPanelArray solar, BatteryBank battery, ExternalGrid grid,
                           LoadController loadController, TelemetryLogger telemetry) {
        this.solar = solar;
        this.battery = battery;
        this.grid = grid;
        this.loadController = loadController;
        this.telemetry = telemetry;
    }

    public EnergySummary balancePower(double demandKw) {
        double remaining = demandKw;

        double fromSolar = solar.drawPower(remaining);
        remaining -= fromSolar;

        double fromBattery = battery.discharge(remaining);
        remaining -= fromBattery;

        double fromGrid = grid.importPower(remaining);
        remaining -= fromGrid;

        boolean shed = false;
        if (remaining > 0.001) {
            shed = true;
            loadController.shedNonEssentialCircuits(remaining);
            telemetry.logEvent(String.format("Power deficit of %.2f kW recorded.", remaining));
        } else {
            telemetry.logEvent("Demand of " + demandKw + " kW fully satisfied.");
        }

        return new EnergySummary(fromSolar, fromBattery, fromGrid, remaining, shed);
    }
}

public class Main {
    public static void main(String[] args) {
        
        MicrogridFacade grid = new MicrogridFacade(
                new SolarPanelArray(30.0),
                new BatteryBank(40.0),
                new ExternalGrid(25.0),
                new LoadController(),
                new TelemetryLogger()
        );

        System.out.println("Run 1 (50 kW demand): " + grid.balancePower(50.0));
        System.out.println("\nRun 2 (40 kW demand - exceeds remaining capacity): " + grid.balancePower(40.0));
    }
}