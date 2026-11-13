package task19;

enum Priority { CRITICAL, URGENT, STANDARD }

record AdmissionRecord(String patientName, Priority priority, String assignedBed, String notes) {}

class VitalsMonitor {
    public void logVitals(String patient, int hr, int bp, double temp) {
        System.out.printf("[Vitals] %s -> HR: %d bpm, BP: %d mmHg, Temp: %.1f°C%n", patient, hr, bp, temp);
    }
}

class TriageClassifier {
    public Priority classify(int heartRate, int systolicBp, double tempC) {
        if (heartRate > 130 || systolicBp < 85 || tempC >= 40.0) {
            return Priority.CRITICAL;
        }
        if (heartRate > 105 || systolicBp > 160 || tempC >= 38.5) {
            return Priority.URGENT;
        }
        return Priority.STANDARD;
    }
}

class BedAllocator {
    private int icuBedsAvailable = 1;
    private int erBaysAvailable = 5;

    public String allocateBed(Priority priority) {
        if (priority == Priority.CRITICAL) {
            if (icuBedsAvailable > 0) {
                icuBedsAvailable--;
                return "ICU-Bed-" + (1 - icuBedsAvailable);
            } else if (erBaysAvailable > 0) {
                erBaysAvailable--;
                return "Monitored-ER-Bay (WARNING: ICU FULL)";
            }
        }
        if (erBaysAvailable > 0) {
            erBaysAvailable--;
            return "Standard-ER-Bay-" + (5 - erBaysAvailable);
        }
        return "Waiting-Area";
    }
}

class PharmacyDispenser {
    public void orderStarterMeds(String patient, Priority priority) {
        String pack = (priority == Priority.CRITICAL) ? "Emergency Resuscitation Kit" : "Standard Analgesic & IV Fluids";
        System.out.println("[Pharmacy] Pre-ordered " + pack + " for " + patient);
    }
}

class DoctorPager {
    public void pageSpecialist(Priority priority, String bed) {
        String role = (priority == Priority.CRITICAL) ? "Trauma Attending" : "On-Call ER Physician";
        System.out.println("[Pager] Paged " + role + " to " + bed + "\n");
    }
}

class EmergencyAdmissionFacade {
    private final VitalsMonitor vitals;
    private final TriageClassifier classifier;
    private final BedAllocator beds;
    private final PharmacyDispenser pharmacy;
    private final DoctorPager pager;

    public EmergencyAdmissionFacade(VitalsMonitor vitals, TriageClassifier classifier, BedAllocator beds,
                                    PharmacyDispenser pharmacy, DoctorPager pager) {
        this.vitals = vitals;
        this.classifier = classifier;
        this.beds = beds;
        this.pharmacy = pharmacy;
        this.pager = pager;
    }

    public AdmissionRecord admitPatient(String patientName, int heartRate, int systolicBp, double tempC) {
        vitals.logVitals(patientName, heartRate, systolicBp, tempC);
        Priority priority = classifier.classify(heartRate, systolicBp, tempC);
        String bed = beds.allocateBed(priority);
        pharmacy.orderStarterMeds(patientName, priority);
        pager.pageSpecialist(priority, bed);

        String note = bed.contains("WARNING") ? "Fallback bed used due to ICU capacity" : "Normal admission";
        return new AdmissionRecord(patientName, priority, bed, note);
    }
}

public class Main {
    public static void main(String[] args) {
        EmergencyAdmissionFacade er = new EmergencyAdmissionFacade(
                new VitalsMonitor(), new TriageClassifier(), new BedAllocator(),
                new PharmacyDispenser(), new DoctorPager()
        );

        System.out.println(er.admitPatient("Patient A", 145, 78, 37.2)); 
        System.out.println(er.admitPatient("Patient B", 138, 80, 39.1)); 
    }
}