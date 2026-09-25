class FahrenheitSensor:
    def read_raw_temperature(self) -> str:
        
        return "77.0 F"

class ICelsiusSensor:
    def get_temperature_in_celsius(self) -> float:
        raise NotImplementedError

class TemperatureSensorAdapter(ICelsiusSensor):
    def __init__(self, fahrenheit_sensor: FahrenheitSensor):
        self._fahrenheit_sensor = fahrenheit_sensor

    def get_temperature_in_celsius(self) -> float:
        
        raw_temp = self._fahrenheit_sensor.read_raw_temperature()
        numeric_str = raw_temp.replace(" F", "").strip()

        fahrenheit = float(numeric_str)

        celsius = (fahrenheit - 32.0) * (5.0 / 9.0)

        return round(celsius, 2)