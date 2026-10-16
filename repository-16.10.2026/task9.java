interface Pizza {
    String getDetails();
    int getPrice();
}

class Margherita implements Pizza {
    public String getDetails() {
        return "Margherita";
    }
    public int getPrice() {
        return 200;
    }
}

class ExtraCheese implements Pizza {
    Pizza p;
    public ExtraCheese(Pizza p) { this.p = p; }
    public String getDetails() { return p.getDetails() + ", Extra Cheese"; }
    public int getPrice() { return p.getPrice() + 40; }
}

class Olives implements Pizza {
    Pizza p;
    public Olives(Pizza p) { this.p = p; }
    public String getDetails() { return p.getDetails() + ", Olives"; }
    public int getPrice() { return p.getPrice() + 30; }
}

class Jalapenos implements Pizza {
    Pizza p;
    public Jalapenos(Pizza p) { this.p = p; }
    public String getDetails() { return p.getDetails() + ", Jalapenos"; }
    public int getPrice() { return p.getPrice() + 25; }
}

class Main {
    public static void main(String[] args) {
        Pizza myPizza = new Jalapenos(new Olives(new ExtraCheese(new Margherita())));
        System.out.println(myPizza.getDetails());
        System.out.println(myPizza.getPrice());
    }
}