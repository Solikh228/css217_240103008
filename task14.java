package task14;

class ProfileRegistry {
    public void createProfile(String name, int age) {
        System.out.println("[Registry] Profile created for " + name + " (age " + age + ").");
    }
}

class HealthScreening {
    public boolean checkEligibility(int age, boolean medicalCleared) {
        return age >= 16 && medicalCleared;
    }
}

class LockerAssigner {
    private int nextLocker = 101;

    public int assignLocker() {
        return nextLocker++;
    }
}

class AccessCardIssuer {
    private int cardCounter = 5000;

    public String issueRfidCard() {
        return "RFID-" + (++cardCounter);
    }
}

class GymOnboardingFacade {
    private final ProfileRegistry registry;
    private final HealthScreening screening;
    private final LockerAssigner lockerAssigner;
    private final AccessCardIssuer cardIssuer;

    public GymOnboardingFacade(ProfileRegistry registry, HealthScreening screening,
                               LockerAssigner lockerAssigner, AccessCardIssuer cardIssuer) {
        this.registry = registry;
        this.screening = screening;
        this.lockerAssigner = lockerAssigner;
        this.cardIssuer = cardIssuer;
    }

    public String registerNewMember(String name, int age, boolean medicalCleared) {
        if (age < 16) {
            return "Registration rejected for " + name + ": Minimum age is 16.";
        }
        if (!screening.checkEligibility(age, medicalCleared)) {
            return "Registration rejected for " + name + ": Medical clearance required.";
        }

        registry.createProfile(name, age);
        int locker = lockerAssigner.assignLocker();
        String rfid = cardIssuer.issueRfidCard();

        return "Welcome, " + name + "! Locker #" + locker + " assigned. Access Card: " + rfid;
    }
}

public class Main {
    public static void main(String[] args) {
        GymOnboardingFacade gym = new GymOnboardingFacade(
                new ProfileRegistry(), new HealthScreening(), new LockerAssigner(), new AccessCardIssuer()
        );

        System.out.println(gym.registerNewMember("Timur", 15, true));
        System.out.println(gym.registerNewMember("Dias", 20, false));
        System.out.println(gym.registerNewMember("Aigerim", 19, true));
    }
}