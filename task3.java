package task03;

class PowerSupply {
    public void supplyPower() { System.out.println("[PSU] Power rails stabilized."); }
    public void cutPower() { System.out.println("[PSU] Power cut off."); }
}

class BIOS {
    public void runPost() { System.out.println("[BIOS] POST check passed. Hardware OK."); }
}

class HardDrive {
    public void spinUp() { System.out.println("[HardDrive] Disk spinning up..."); }
    public void readBootSector() { System.out.println("[HardDrive] Reading MBR/GPT boot sector."); }
    public void parkHeads() { System.out.println("[HardDrive] Read/write heads parked safely."); }
}

class OperatingSystem {
    public void loadKernel() { System.out.println("[OS] Kernel loaded. Welcome to desktop."); }
    public void shutdown() { System.out.println("[OS] Closing background services and halting kernel..."); }
}

class ComputerFacade {
    private final PowerSupply psu;
    private final BIOS bios;
    private final HardDrive hdd;
    private final OperatingSystem os;

    public ComputerFacade(PowerSupply psu, BIOS bios, HardDrive hdd, OperatingSystem os) {
        this.psu = psu;
        this.bios = bios;
        this.hdd = hdd;
        this.os = os;
    }

    public void startComputer() {
        System.out.println("=== Booting PC ===");
        psu.supplyPower();
        bios.runPost();
        hdd.spinUp();
        hdd.readBootSector();
        os.loadKernel();
    }

    public void shutDownComputer() {
        System.out.println("\n=== Shutting Down PC ===");
        os.shutdown();
        hdd.parkHeads();
        psu.cutPower();
    }
}

public class Main {
    public static void main(String[] args) {
        ComputerFacade pc = new ComputerFacade(
                new PowerSupply(), new BIOS(), new HardDrive(), new OperatingSystem()
        );
        pc.startComputer();
        pc.shutDownComputer();
    }
}