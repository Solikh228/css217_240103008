package task04;

class CeilingLight {
    public void on() { System.out.println("[Light] Ceiling light turned ON."); }
    public void off() { System.out.println("[Light] Ceiling light turned OFF."); }
}

class Thermostat {
    public void setTemperature(int temp) {
        System.out.println("[Thermostat] Target temperature set to " + temp + "°C.");
    }
}

class WindowBlinds {
    public void open() { System.out.println("[Blinds] Opening window blinds."); }
    public void close() { System.out.println("[Blinds] Closing window blinds."); }
}

class DoorLock {
    public void lock() { System.out.println("[DoorLock] Main door LOCKED."); }
    public void unlock() { System.out.println("[DoorLock] Main door UNLOCKED."); }
}

class SmartRoomFacade {
    private final CeilingLight light;
    private final Thermostat thermostat;
    private final WindowBlinds blinds;
    private final DoorLock lock;

    public SmartRoomFacade(CeilingLight light, Thermostat thermostat, WindowBlinds blinds, DoorLock lock) {
        this.light = light;
        this.thermostat = thermostat;
        this.blinds = blinds;
        this.lock = lock;
    }

    public void activateNightMode() {
        System.out.println("--- Activating Night Mode ---");
        light.off();
        blinds.close();
        lock.lock();
        thermostat.setTemperature(19);
    }

    public void activateMorningMode() {
        System.out.println("\n--- Activating Morning Mode ---");
        blinds.open();
        light.on();
        thermostat.setTemperature(22);
    }
}

public class Main {
    public static void main(String[] args) {
        SmartRoomFacade room = new SmartRoomFacade(
                new CeilingLight(), new Thermostat(), new WindowBlinds(), new DoorLock()
        );
        room.activateNightMode();
        room.activateMorningMode();
    }
}