import java.util.*;

record TaxLot(String lotId, String ticker, double shares, double costBasis) {}

interface AllocationStrategy {
    Map<String, Double> targetWeights();
}

class AggressiveEquityStrategy implements AllocationStrategy {
    public Map<String, Double> targetWeights() { return Map.of("SPY", 0.60, "QQQ", 0.30, "BND", 0.10); }
}

class BalancedIndexStrategy implements AllocationStrategy {
    public Map<String, Double> targetWeights() { return Map.of("SPY", 0.50, "QQQ", 0.10, "BND", 0.40); }
}

class CapitalPreservationStrategy implements AllocationStrategy {
    public Map<String, Double> targetWeights() { return Map.of("SPY", 0.20, "QQQ", 0.00, "BND", 0.80); }
}

class MarketPriceFeed {
    Map<String, Double> getPrices() { return Map.of("SPY", 500.0, "QQQ", 420.0, "BND", 75.0); }
}

class DriftAnalyzer {
    Map<String, Double> computeDeltas(Map<String, Double> currentVals, Map<String, Double> targets) {
        double total = currentVals.values().stream().mapToDouble(Double::doubleValue).sum();
        Map<String, Double> deltas = new LinkedHashMap<>();
        targets.forEach((t, w) -> deltas.put(t, Math.round((total * w - currentVals.getOrDefault(t, 0.0)) * 100.0) / 100.0));
        return deltas;
    }
}

class TaxLotEvaluator {
    List<TaxLot> prioritizeLossLots(List<TaxLot> lots, Map<String, Double> prices) {
        return lots.stream().sorted(Comparator.comparingDouble(l -> prices.get(l.ticker()) - l.costBasis())).toList();
    }
}

class OrderExecutionBroker {
    List<String> submitOrders(Map<String, Double> deltas, List<TaxLot> lots) {
        List<String> out = new ArrayList<>();
        deltas.forEach((ticker, delta) -> {
            if (Math.abs(delta) > 1.0) out.add((delta > 0 ? "BUY " : "SELL ") + Math.abs(delta) + " " + ticker);
        });
        return out;
    }
}

class PortfolioFacade {
    private AllocationStrategy strategy;
    private final MarketPriceFeed feed = new MarketPriceFeed();
    private final DriftAnalyzer drift = new DriftAnalyzer();
    private final TaxLotEvaluator taxEval = new TaxLotEvaluator();
    private final OrderExecutionBroker broker = new OrderExecutionBroker();

    PortfolioFacade(AllocationStrategy strategy) { this.strategy = strategy; }
    void setStrategy(AllocationStrategy strategy) { this.strategy = strategy; }

    List<String> rebalanceAccount(String accountId) {
        Map<String, Double> prices = feed.getPrices();
        List<TaxLot> lots = List.of(
            new TaxLot("1", "SPY", 10, 480.0),
            new TaxLot("2", "QQQ", 10, 450.0),
            new TaxLot("3", "BND", 10, 80.0)
        );
        Map<String, Double> curVals = new LinkedHashMap<>();
        for (TaxLot l : lots) curVals.put(l.ticker(), l.shares() * prices.get(l.ticker()));
        Map<String, Double> deltas = drift.computeDeltas(curVals, strategy.targetWeights());
        return broker.submitOrders(deltas, taxEval.prioritizeLossLots(lots, prices));
    }
}
vibe code