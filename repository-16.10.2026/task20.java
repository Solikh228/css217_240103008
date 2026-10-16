interface Membership {
    String getPerks();
    double getMonthlyFee();
}

class BasicGymAccess implements Membership {
    public String getPerks() { return "Gym Floor"; }
    public double getMonthlyFee() { return 1000.0; }
}

class PoolAccessDecorator implements Membership {
    Membership m;
    public PoolAccessDecorator(Membership m) { this.m = m; }
    public String getPerks() { return m.getPerks() + ", Pool"; }
    public double getMonthlyFee() { return m.getMonthlyFee() + 400.0; }
}

class SaunaDecorator implements Membership {
    Membership m;
    public SaunaDecorator(Membership m) { this.m = m; }
    public String getPerks() { return m.getPerks() + ", Sauna"; }
    public double getMonthlyFee() { return m.getMonthlyFee() + 250.0; }
}

class PersonalTrainerDecorator implements Membership {
    Membership m;
    public PersonalTrainerDecorator(Membership m) { this.m = m; }
    public String getPerks() { return m.getPerks() + ", Personal Trainer"; }
    public double getMonthlyFee() { return m.getMonthlyFee() + 1500.0; }
}