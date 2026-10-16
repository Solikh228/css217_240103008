interface Beverage {
    String getDescription();
    double getCost();
}

class Espresso implements Beverage {
    public String getDescription() { return "Espresso"; }
    public double getCost() { return 100.0; }
}

abstract class BeverageDecorator implements Beverage {
    protected Beverage beverage; 
    
    public BeverageDecorator(Beverage bev) {
        this.beverage = bev;
    }
}

class MilkDecorator extends BeverageDecorator {
    public MilkDecorator(Beverage bev) { super(bev); }
    public String getDescription() { return beverage.getDescription() + ", Milk"; }
    public double getCost() { return beverage.getCost() + 20.0; }
}

class CaramelDecorator extends BeverageDecorator {
    public CaramelDecorator(Beverage bev) { super(bev); }
    public String getDescription() { return beverage.getDescription() + ", Caramel"; }
    public double getCost() { return beverage.getCost() + 35.0; }
}