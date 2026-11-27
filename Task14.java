import java.util.List;

interface CalorieCalculationStrategy {
    double estimateKcal(double weightKg, double durationMin, double avgHr, List<Double> samples);
}

class CardioMetStrategy implements CalorieCalculationStrategy {
    public double estimateKcal(double w, double dur, double avgHr, List<Double> s) {
        double met = avgHr > 140 ? 8.0 : 5.5;
        return Math.round(met * 3.5 * w / 200.0 * dur * 100.0) / 100.0;
    }
}

class StrengthLoadStrategy implements CalorieCalculationStrategy {
    public double estimateKcal(double w, double dur, double avgHr, List<Double> s) {
        double avg = s.isEmpty() ? 0 : s.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return Math.round((4.5 + avg * 0.02) * 3.5 * w / 200.0 * dur * 100.0) / 100.0;
    }
}

class HiitIntervalStrategy implements CalorieCalculationStrategy {
    public double estimateKcal(double w, double dur, double avgHr, List<Double> s) {
        long peaks = s.stream().filter(hr -> hr > 165).count();
        return Math.round((10.5 * 3.5 * w / 200.0 * dur + peaks * 1.8) * 100.0) / 100.0;
    }
}

class HeartRateSensor {
    double averageBpm(List<Double> s) { return s.isEmpty() ? 0 : s.stream().mapToDouble(Double::doubleValue).average().orElse(0); }
}

class SessionTimer {
    double getDurationMinutes(List<Double> s) { return s.size() * 0.5; }
}

class UserBiometricsStore {
    double getWeightKg(String userId) { return 63.0; }
}

class ActivityLogRepository {
    void save(String userId, double kcal, double min) {
        System.out.println("Workout saved for " + userId + ": " + min + "m, " + kcal + " kcal");
    }
}

class WorkoutSessionFacade {
    private CalorieCalculationStrategy strategy;
    private final HeartRateSensor hrSensor = new HeartRateSensor();
    private final SessionTimer timer = new SessionTimer();
    private final UserBiometricsStore biometrics = new UserBiometricsStore();
    private final ActivityLogRepository repo = new ActivityLogRepository();

    WorkoutSessionFacade(CalorieCalculationStrategy strategy) { this.strategy = strategy; }
    void setStrategy(CalorieCalculationStrategy strategy) { this.strategy = strategy; }

    double completeWorkout(String userId, List<Double> telemetrySamples) {
        double w = biometrics.getWeightKg(userId);
        double hr = hrSensor.averageBpm(telemetrySamples);
        double dur = timer.getDurationMinutes(telemetrySamples);
        double kcal = strategy.estimateKcal(w, dur, hr, telemetrySamples);
        repo.save(userId, kcal, dur);
        return kcal;
    }
}