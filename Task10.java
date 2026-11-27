import java.util.List;

record Coord(double lat, double lon) {}

interface RoutingStrategy {
    List<Coord> computeRoute(Coord origin, Coord destination, double trafficFactor);
}

class FastestTimeRouting implements RoutingStrategy {
    public List<Coord> computeRoute(Coord o, Coord d, double tf) {
        return List.of(o, new Coord((o.lat() + d.lat()) / 2 + 0.02 * tf, (o.lon() + d.lon()) / 2), d);
    }
}

class ShortestDistanceRouting implements RoutingStrategy {
    public List<Coord> computeRoute(Coord o, Coord d, double tf) { return List.of(o, d); }
}

class AvoidTollsRouting implements RoutingStrategy {
    public List<Coord> computeRoute(Coord o, Coord d, double tf) {
        return List.of(o, new Coord(o.lat() - 0.05, d.lon() + 0.05), d);
    }
}

class GpsTracker {
    Coord currentPosition() { return new Coord(43.2389, 76.8897); }
}

class MapTileLoader {
    void preloadCorridor(List<Coord> pts) { System.out.println("Loaded tiles for " + pts.size() + " points"); }
}

class TrafficFeedClient {
    double getCongestionMultiplier() { return 1.25; }
}

class VoiceGuidanceUnit {
    void announceRoute(List<Coord> pts) { System.out.println("Voice guidance: " + pts); }
}

class NavigationFacade {
    private RoutingStrategy strategy;
    private final GpsTracker gps = new GpsTracker();
    private final MapTileLoader tiles = new MapTileLoader();
    private final TrafficFeedClient traffic = new TrafficFeedClient();
    private final VoiceGuidanceUnit voice = new VoiceGuidanceUnit();
    private Coord destination;

    NavigationFacade(RoutingStrategy strategy) { this.strategy = strategy; }

    List<Coord> startNavigation(Coord origin, Coord destination) {
        this.destination = destination;
        return applyRoute(origin, destination);
    }

    List<Coord> swapStrategyMidTrip(RoutingStrategy newStrategy) {
        this.strategy = newStrategy;
        if (destination == null) throw new IllegalStateException("No active trip");
        return applyRoute(gps.currentPosition(), destination);
    }

    private List<Coord> applyRoute(Coord start, Coord end) {
        List<Coord> route = strategy.computeRoute(start, end, traffic.getCongestionMultiplier());
        tiles.preloadCorridor(route);
        voice.announceRoute(route);
        return route;
    }
}