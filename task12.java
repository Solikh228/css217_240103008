package task12;

class DoorSensor {
    private boolean closed = false;

    public void closeDoor() {
        closed = true;
        System.out.println("[DoorSensor] Door closed and locked.");
    }

    public void openDoor() {
        closed = false;
        System.out.println("[DoorSensor] Door unlocked and opened.");
    }

    public boolean isClosed() {
        return closed;
    }
}

class WaterValve {
    public void fill(String temp) {
        System.out.println("[WaterValve] Filling drum with " + temp + " water.");
    }
}

class DetergentDispenser {
    public void dispense(String amount) {
        System.out.println("[Detergent] Dispensing " + amount + " dose of detergent.");
    }
}

class DrumMotor {
    public void spin(int minutes, String speed) {
        System.out.println("[DrumMotor] Spinning (" + speed + ") for " + minutes + " minutes.");
    }
}

class DrainPump {
    public void drain() {
        System.out.println("[DrainPump] Draining water from drum.");
    }
}

class WashingMachineFacade {
    private final DoorSensor door;
    private final WaterValve valve;
    private final DetergentDispenser detergent;
    private final DrumMotor motor;
    private final DrainPump pump;

    public WashingMachineFacade(DoorSensor door, WaterValve valve, DetergentDispenser detergent, DrumMotor motor, DrainPump pump) {
        this.door = door;
        this.valve = valve;
        this.detergent = detergent;
        this.motor = motor;
        this.pump = pump;
    }

    public void runQuickWash() {
        if (!door.isClosed()) {
            System.out.println("Error: Cannot start Quick Wash. Close the door first!");
            return;
        }
        System.out.println("=== Starting Quick Wash ===");
        valve.fill("cold");
        detergent.dispense("standard");
        motor.spin(15, "gentle");
        pump.drain();
        System.out.println("Quick Wash finished.\n");
    }

    public void runHeavyWash() {
        if (!door.isClosed()) {
            System.out.println("Error: Cannot start Heavy Wash. Close the door first!");
            return;
        }
        System.out.println("=== Starting Heavy Wash ===");
        valve.fill("warm");
        detergent.dispense("extra");
        motor.spin(45, "high-speed");
        pump.drain();
        System.out.println("Heavy Wash finished.\n");
    }
}

public class Main {
    public static void main(String[] args) {
        DoorSensor door = new DoorSensor();
        WashingMachineFacade washer = new WashingMachineFacade(
                door, new WaterValve(), new DetergentDispenser(), new DrumMotor(), new DrainPump()
        );

        washer.runQuickWash(); 
        door.closeDoor();
        washer.runQuickWash();
        washer.runHeavyWash();
        door.openDoor();
    }
}