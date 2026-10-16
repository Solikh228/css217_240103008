interface CelsiusThermometer {
    double getTemperatureC();
}

class LegacyFahrenheitSensor {
    public double readFahrenheit() { 
        return 100.0;
    }
}

class SensorAdapter implements CelsiusThermometer {
    private LegacyFahrenheitSensor sensor;

    public SensorAdapter(LegacyFahrenheitSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public double getTemperatureC() {
        
        return (sensor.readFahrenheit() - 32.0) * 5.0 / 9.0;
    }
}