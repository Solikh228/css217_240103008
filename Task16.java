record LoanApplication(String applicantId, double amount, double monthlyIncome, double monthlyDebt, double cashflowGrowth) {}
record UnderwritingReport(String applicantId, boolean approved, double riskScore, String reason) {}

interface RiskScoringStrategy {
    UnderwritingReport score(int creditScore, double dti, LoanApplication app);
}

class ConservativeCreditStrategy implements RiskScoringStrategy {
    public UnderwritingReport score(int cs, double dti, LoanApplication app) {
        return new UnderwritingReport(app.applicantId(), cs >= 740 && dti <= 0.30, cs, "Conservative check");
    }
}

class StandardRetailStrategy implements RiskScoringStrategy {
    public UnderwritingReport score(int cs, double dti, LoanApplication app) {
        return new UnderwritingReport(app.applicantId(), cs >= 660 && dti <= 0.42, cs * (1.0 - dti), "Retail scorecard");
    }
}

class CashFlowGrowthStrategy implements RiskScoringStrategy {
    public UnderwritingReport score(int cs, double dti, LoanApplication app) {
        double adj = cs + app.cashflowGrowth() * 500.0;
        return new UnderwritingReport(app.applicantId(), adj >= 680 && dti <= 0.50, adj, "Cash-flow check");
    }
}

class CreditBureauAdapter {
    int fetchScore(String id) { return 715; }
}

class DebtToIncomeCalculator {
    double compute(double debt, double income) { return debt / Math.max(1.0, income); }
}

class FraudScreeningEngine {
    boolean hasFraudFlags(LoanApplication app) { return app.applicantId().startsWith("FRAUD"); }
}

class ComplianceAuditLogger {
    void appendImmutable(UnderwritingReport r) { System.out.println("AUDIT: " + r); }
}

class UnderwritingFacade {
    private RiskScoringStrategy strategy;
    private final CreditBureauAdapter bureau = new CreditBureauAdapter();
    private final DebtToIncomeCalculator dtiCalc = new DebtToIncomeCalculator();
    private final FraudScreeningEngine fraud = new FraudScreeningEngine();
    private final ComplianceAuditLogger audit = new ComplianceAuditLogger();

    UnderwritingFacade(RiskScoringStrategy strategy) { this.strategy = strategy; }
    void setStrategy(RiskScoringStrategy strategy) { this.strategy = strategy; }

    UnderwritingReport evaluate(LoanApplication app) {
        if (fraud.hasFraudFlags(app)) {
            UnderwritingReport r = new UnderwritingReport(app.applicantId(), false, 0.0, "SHORT_CIRCUIT: Fraud");
            audit.appendImmutable(r);
            return r;
        }
        int score = bureau.fetchScore(app.applicantId());
        double dti = dtiCalc.compute(app.monthlyDebt(), app.monthlyIncome());
        UnderwritingReport r = strategy.score(score, dti, app);
        audit.appendImmutable(r);
        return r;
    }
}