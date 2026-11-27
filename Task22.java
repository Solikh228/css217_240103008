import java.util.List;

record PatientRecord(String patientId, int age, int heartRate, int spo2, String specialtyNeeded) {}

interface TriagePriorityStrategy {
    int calculateUrgency(PatientRecord record);
}

class StandardSeverityIndex implements TriagePriorityStrategy {
    public int calculateUrgency(PatientRecord r) { return (r.spo2() < 90 || r.heartRate() > 130) ? 1 : 3; }
}

class PediatricFastTrack implements TriagePriorityStrategy {
    public int calculateUrgency(PatientRecord r) { return r.age() < 12 ? 1 : 2; }
}

class MassCasualtyProtocol implements TriagePriorityStrategy {
    public int calculateUrgency(PatientRecord r) { return r.spo2() < 85 ? 2 : 1; }
}

interface BedMatchingStrategy {
    String selectWard(PatientRecord record, List<String> availableWards);
}

class ClosestUnitMatch implements BedMatchingStrategy {
    public String selectWard(PatientRecord r, List<String> wards) { return wards.get(0); }
}

class SpecialistWardMatch implements BedMatchingStrategy {
    public String selectWard(PatientRecord r, List<String> wards) {
        return wards.stream()
            .filter(w -> w.toUpperCase().contains(r.specialtyNeeded().toUpperCase()))
            .findFirst()
            .orElse(wards.get(0));
    }
}

class VitalsMonitor {
    void validateVitals(PatientRecord r) { System.out.println("Vitals: " + r.patientId() + " SpO2=" + r.spo2()); }
}

class LabOrderDispatcher {
    void orderStatPanel(String patientId, int urgency) { System.out.println("STAT labs for " + patientId + " P" + urgency); }
}

class WardCapacityTracker {
    List<String> getOpenWards() { return List.of("ER_BAY_1", "CARDIOLOGY_ICU", "PEDIATRIC_WARD"); }
    String reserveBed(String ward, String patientId) { return ward + "_BED_02"; }
}

class AdmissionNotifier {
    void notifyStaff(String patientId, String bedId, int urgency) {
        System.out.println("STAFF ALERT: " + patientId + " -> " + bedId + " [Urgency " + urgency + "]");
    }
}

class EmergencyIntakeFacade {
    private TriagePriorityStrategy triage;
    private BedMatchingStrategy bedMatcher;
    private final VitalsMonitor vitals = new VitalsMonitor();
    private final LabOrderDispatcher labs = new LabOrderDispatcher();
    private final WardCapacityTracker capacity = new WardCapacityTracker();
    private final AdmissionNotifier notifier = new AdmissionNotifier();

    EmergencyIntakeFacade(TriagePriorityStrategy triage, BedMatchingStrategy bedMatcher) {
        this.triage = triage;
        this.bedMatcher = bedMatcher;
    }

    void setStrategies(TriagePriorityStrategy triage, BedMatchingStrategy bedMatcher) {
        this.triage = triage;
        this.bedMatcher = bedMatcher;
    }

    String admitPatient(PatientRecord patientRecord) {
        vitals.validateVitals(patientRecord);
        int urgency = triage.calculateUrgency(patientRecord);
        String ward = bedMatcher.selectWard(patientRecord, capacity.getOpenWards());
        String bed = capacity.reserveBed(ward, patientRecord.patientId());
        labs.orderStatPanel(patientRecord.patientId(), urgency);
        notifier.notifyStaff(patientRecord.patientId(), bed, urgency);
        return bed;
    }
}
//vibe code