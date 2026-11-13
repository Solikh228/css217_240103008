package task02;

class WaterHeater {
    public void heatWater(int celsius) {
        System.out.println("[WaterHeater] Heating water to " + celsius + "°C...");
    }
    public void dispenseExtraWater(int ml) {
        System.out.println("[WaterHeater] Adding " + ml + "ml of hot water.");
    }
}

class CoffeeGrinder {
    public void grindBeans(String grindSize) {
        System.out.println("[CoffeeGrinder] Grinding beans (" + grindSize + " grind).");
    }
}

class BrewUnit {
    public void brew(int seconds) {
        System.out.println("[BrewUnit] Extracting coffee for " + seconds + " seconds...");
    }
}

class CupDispenser {
    public void dispenseCup() {
        System.out.println("[CupDispenser] Dropped a fresh cup onto the tray.");
    }
}

class CoffeeMachineFacade {
    private final WaterHeater heater;
    private final CoffeeGrinder grinder;
    private final BrewUnit brewUnit;
    private final CupDispenser dispenser;

    public CoffeeMachineFacade(WaterHeater heater, CoffeeGrinder grinder, BrewUnit brewUnit, CupDispenser dispenser) {
        this.heater = heater;
        this.grinder = grinder;
        this.brewUnit = brewUnit;
        this.dispenser = dispenser;
    }

    public void brewEspresso() {
        System.out.println("--- Making Espresso ---");
        dispenser.dispenseCup();
        grinder.grindBeans("fine");
        heater.heatWater(92);
        brewUnit.brew(25);
        System.out.println("Espresso is ready!\n");
    }

    public void brewAmericano() {
        System.out.println("--- Making Americano ---");
        dispenser.dispenseCup();
        grinder.grindBeans("medium");
        heater.heatWater(95);
        brewUnit.brew(40);
        heater.dispenseExtraWater(100);
        System.out.println("Americano is ready!\n");
    }
}

public class Main {
    public static void main(String[] args) {
        CoffeeMachineFacade machine = new CoffeeMachineFacade(
                new WaterHeater(), new CoffeeGrinder(), new BrewUnit(), new CupDispenser()
        );
        machine.brewEspresso();
        machine.brewAmericano();
    }
}
