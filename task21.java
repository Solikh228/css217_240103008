package task21;

import java.util.*;

record RegistrationResult(boolean confirmed, String message, String badgeId) {}

class EligibilityChecker {
    public boolean isEligible(String studentId) {
        return studentId != null && studentId.startsWith("24");
    }
}

class QuotaManager {
    private final Map<String, Integer> trackSeats = new HashMap<>(Map.of(
            "AI-Workshop", 5,
            "Robotics-Lab", 2
    ));

    public boolean reserveSeat(String track) {
        int left = trackSeats.getOrDefault(track, 0);
        if (left <= 0) return false;
        trackSeats.put(track, left - 1);
        System.out.println("[QuotaManager] Reserved seat in " + track + " (remaining: " + (left - 1) + ")");
        return true;
    }

    public void releaseSeat(String track) {
        trackSeats.put(track, trackSeats.getOrDefault(track, 0) + 1);
        System.out.println("[QuotaManager] Released seat back to " + track + " (now: " + trackSeats.get(track) + ")");
    }
}

class WorkshopScheduler {
    public String getScheduleInfo(String track) {
        return track + " @ 10:00 AM";
    }
}

class ShuttleAllocator {
    private int shuttleSeatsLeft = 1;

    public boolean reserveRoundTrip(String fromCampus, String toCampus) {
        if (shuttleSeatsLeft <= 0) {
            System.out.println("[Shuttle] No shuttle seats left between " + fromCampus + " and " + toCampus);
            return false;
        }
        shuttleSeatsLeft--;
        System.out.println("[Shuttle] Reserved round-trip seat (" + fromCampus + " <-> " + toCampus + ")");
        return true;
    }
}

class BadgeGenerator {
    public String createBadge(String studentId, String track, boolean needsShuttle) {
        return "BADGE-" + studentId + "-" + track + (needsShuttle ? "-SHUTTLE" : "-LOCAL");
    }
}

class EventLogisticsFacade {
    private final EligibilityChecker eligibility;
    private final QuotaManager quotas;
    private final WorkshopScheduler scheduler;
    private final ShuttleAllocator shuttle;
    private final BadgeGenerator badgeGen;

    public EventLogisticsFacade(EligibilityChecker eligibility, QuotaManager quotas, WorkshopScheduler scheduler,
                                ShuttleAllocator shuttle, BadgeGenerator badgeGen) {
        this.eligibility = eligibility;
        this.quotas = quotas;
        this.scheduler = scheduler;
        this.shuttle = shuttle;
        this.badgeGen = badgeGen;
    }

    public RegistrationResult registerAttendee(String studentId, String homeCampus, String eventCampus, String track) {
        if (!eligibility.isEligible(studentId)) {
            return new RegistrationResult(false, "Student " + studentId + " is not eligible.", null);
        }

        if (!quotas.reserveSeat(track)) {
            return new RegistrationResult(false, "Track '" + track + "' is full.", null);
        }

        boolean needsShuttle = !homeCampus.equalsIgnoreCase(eventCampus);
        if (needsShuttle) {
            if (!shuttle.reserveRoundTrip(homeCampus, eventCampus)) {
                
                quotas.releaseSeat(track);
                return new RegistrationResult(false, "Declined: Inter-campus shuttle is full. Workshop seat released.", null);
            }
        }

        String schedule = scheduler.getScheduleInfo(track);
        String badge = badgeGen.createBadge(studentId, track, needsShuttle);
        return new RegistrationResult(true, "Confirmed for " + schedule, badge);
    }
}

public class Main {
    public static void main(String[] args) {
        EventLogisticsFacade logistics = new EventLogisticsFacade(
                new EligibilityChecker(), new QuotaManager(), new WorkshopScheduler(),
                new ShuttleAllocator(), new BadgeGenerator()
        );

        
        System.out.println(logistics.registerAttendee("240103008", "Kaskelen", "Almaty-Center", "Robotics-Lab"));
        System.out.println();

        
        System.out.println(logistics.registerAttendee("240103019", "Kaskelen", "Almaty-Center", "Robotics-Lab"));
        System.out.println();

        System.out.println(logistics.registerAttendee("240103025", "Almaty-Center", "Almaty-Center", "Robotics-Lab"));
    }
}