interface Car {
    String getConfiguration();
    double getPrice();
}

class BaseSedan implements Car {
    public String getConfiguration() { return "Standard Sedan"; }
    public double getPrice() { return 15000.0; }
}

class SunroofDecorator implements Car {
    Car c;
    public SunroofDecorator(Car c) { this.c = c; }
    public String getConfiguration() { return c.getConfiguration() + ", Sunroof"; }
    public double getPrice() { return c.getPrice() + 1200.0; }
}

class AlloyWheelsDecorator implements Car {
    Car c;
    public AlloyWheelsDecorator(Car c) { this.c = c; }
    public String getConfiguration() { return c.getConfiguration() + ", Alloy Wheels"; }
    public double getPrice() { return c.getPrice() + 800.0; }
}

class DashcamDecorator implements Car {
    Car c;
    public DashcamDecorator(Car c) { this.c = c; }
    public String getConfiguration() { return c.getConfiguration() + ", Dashcam"; }
    public double getPrice() { return c.getPrice() + 300.0; }
}