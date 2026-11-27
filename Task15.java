record HvacCommand(boolean compressorOn, int fanSpeedPct, int damperOpenPct) {}

interface ClimateStrategy {
    HvacCommand evaluate(double tempC, double humidityPct);
}

class EcoAwayStrategy implements ClimateStrategy {
    public HvacCommand evaluate(double t, double h) {
        boolean on = t > 28.0 || t < 16.0;
        return new HvacCommand(on, on ? 30 : 0, 25);
    }
}

class ComfortHoldStrategy implements ClimateStrategy {
    public HvacCommand evaluate(double t, double h) {
        boolean on = Math.abs(t - 22.0) > 0.8 || h > 60.0;
        return new HvacCommand(on, on ? 60 : 20, 60);
    }
}

class RapidPreconditionStrategy implements ClimateStrategy {
    public HvacCommand evaluate(double t, double h) { return new HvacCommand(true, 100, 100); }
}

class ThermostatSensor {
    double readCelsius(String zoneId) { return 24.5; }
}

class HumidityMonitor {
    double readHumidity(String zoneId) { return 52.0; }
}

class DamperActuator {
    void setPosition(String zoneId, int pct) { System.out.println("[" + zoneId + "] Damper=" + pct + "%"); }
}

class CompressorRelay {
    void setState(String zoneId, boolean on, int fan) {
        System.out.println("[" + zoneId + "] Compressor=" + on + ", Fan=" + fan + "%");
    }
}

class HvacControlFacade {
    private ClimateStrategy strategy;
    private final ThermostatSensor thermostat = new ThermostatSensor();
    private final HumidityMonitor humidity = new HumidityMonitor();
    private final DamperActuator damper = new DamperActuator();
    private final CompressorRelay compressor = new CompressorRelay();

    HvacControlFacade(ClimateStrategy strategy) { this.strategy = strategy; }
    void setStrategy(ClimateStrategy strategy) { this.strategy = strategy; }

    HvacCommand regulateZone(String zoneId) {
        double t = thermostat.readCelsius(zoneId);
        double h = humidity.readHumidity(zoneId);
        HvacCommand cmd = strategy.evaluate(t, h);
        damper.setPosition(zoneId, cmd.damperOpenPct());
        compressor.setState(zoneId, cmd.compressorOn(), cmd.fanSpeedPct());
        return cmd;
    }
}