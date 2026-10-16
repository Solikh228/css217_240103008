interface RouteStrategy {
    int estimateMinutes(int distanceKm);
}

class WalkingStrategy implements RouteStrategy {
    public int estimateMinutes(int distanceKm) {
        return distanceKm * 12;
    }
}

class CyclingStrategy implements RouteStrategy {
    public int estimateMinutes(int distanceKm) {
        return distanceKm * 4;
    }
}

class DrivingStrategy implements RouteStrategy {
    public int estimateMinutes(int distanceKm) {
        return (distanceKm * 1) + 10; 
    }
}