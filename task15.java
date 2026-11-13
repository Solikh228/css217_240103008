package task15;

import java.util.HashMap;
import java.util.Map;

class IngredientStock {
    private final Map<String, Integer> stock = new HashMap<>(Map.of(
            "Ribeye Steak", 2,
            "Grilled Chicken", 5,
            "Caesar Salad", 3,
            "Greek Salad", 0
    ));

    public boolean hasItem(String item) {
        return stock.getOrDefault(item, 0) > 0;
    }

    public void useItem(String item) {
        stock.put(item, stock.get(item) - 1);
    }
}

class GrillStation {
    public void cookMain(String mainDish) {
        System.out.println("[Grill] Searing " + mainDish + "...");
    }
}

class SaladStation {
    public void tossSalad(String sideSalad) {
        System.out.println("[SaladStation] Preparing fresh " + sideSalad + ".");
    }
}

class PlatingCounter {
    public void plateCombo(String mainDish, String sideSalad) {
        System.out.println("[Plating] Plated combo: " + mainDish + " with " + sideSalad + ".");
    }
}

class WaiterCallSystem {
    public void notifyServer(int tableNumber) {
        System.out.println("[WaiterCall] Order ready for pickup -> Table #" + tableNumber + "\n");
    }
}

class KitchenFacade {
    private final IngredientStock stock;
    private final GrillStation grill;
    private final SaladStation saladStation;
    private final PlatingCounter plating;
    private final WaiterCallSystem waiterCall;

    public KitchenFacade(IngredientStock stock, GrillStation grill, SaladStation saladStation,
                         PlatingCounter plating, WaiterCallSystem waiterCall) {
        this.stock = stock;
        this.grill = grill;
        this.saladStation = saladStation;
        this.plating = plating;
        this.waiterCall = waiterCall;
    }

    public boolean prepareComboMeal(String mainDish, String sideSalad, int tableNumber) {
        if (!stock.hasItem(mainDish) || !stock.hasItem(sideSalad)) {
            System.out.println("Cannot fulfill order for Table #" + tableNumber + ": Missing ingredients.\n");
            return false;
        }

        stock.useItem(mainDish);
        stock.useItem(sideSalad);

        grill.cookMain(mainDish);
        saladStation.tossSalad(sideSalad);
        plating.plateCombo(mainDish, sideSalad);
        waiterCall.notifyServer(tableNumber);
        return true;
    }
}

public class Main {
    public static void main(String[] args) {
        KitchenFacade kitchen = new KitchenFacade(
                new IngredientStock(), new GrillStation(), new SaladStation(),
                new PlatingCounter(), new WaiterCallSystem()
        );

        kitchen.prepareComboMeal("Ribeye Steak", "Caesar Salad", 4);
        kitchen.prepareComboMeal("Grilled Chicken", "Greek Salad", 7);
    }
}