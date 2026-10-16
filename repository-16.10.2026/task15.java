interface CartesianPoint {
    double getX();
    double getY();
}

class PolarPoint {
    public double getRadius() { return 5.0; }
    public double getAngleRadians() { return 1.57; }
}

class PolarToCartesianAdapter implements CartesianPoint {
    PolarPoint p;

    public PolarToCartesianAdapter(PolarPoint p) {
        this.p = p;
    }

    public double getX() {
        return p.getRadius() * Math.cos(p.getAngleRadians());
    }

    public double getY() {
        return p.getRadius() * Math.sin(p.getAngleRadians());
    }
}