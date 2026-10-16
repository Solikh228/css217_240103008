interface MetricSpeedometer {
    double getSpeedKmh();
}

class ImperialSpeedometer {
    public double getSpeedMph() {
        return 60.0;
    }
}
class SpeedAdapter implements MetricSpeedometer {
    ImperialSpeedometer imp; 

    public SpeedAdapter(ImperialSpeedometer imp) {
        this.imp = imp;
    }

    public double getSpeedKmh() {
        double mph = imp.getSpeedMph();
        return mph * 1.60934;
    }
}