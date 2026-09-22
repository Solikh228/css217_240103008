public class ThermostatAdapter implements SmartDevice {
    private final LegacyThermostat thermostat;

    public ThermostatAdapter(LegacyThermostat thermostat) {
        if (thermostat == null) {
            throw new IllegalArgumentException("LegacyThermostat cannot be null");
        }
        this.thermostat = thermostat;
    }

    @Override
    public void turnOn() {
        String currentDial = thermostat.checkDial();
        if ("IDLE".equals(currentDial)) {
            thermostat.rotateDial("LOW");
        }
    }

    @Override
    public void turnOff() {
        thermostat.rotateDial("IDLE");
    }

    @Override
    public boolean isOn() {
        String dial = thermostat.checkDial();
        if ("LOW".equals(dial) || "MEDIUM".equals(dial) || "MAX".equals(dial)) {
            return true;
        }
        return false;
    }

    @Override
    public int getPowerPercent() {
        String dial = thermostat.checkDial();
        if (dial == null) {
            return -1;
        }
        switch (dial) {
            case "IDLE":
                return 0;
            case "LOW":
                return 33;
            case "MEDIUM":
                return 66;
            case "MAX":
                return 100;
            default:
                return -1;
        }
    }
}