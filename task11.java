package task11;

import java.util.Set;

class TicketVerifier {
    private final Set<String> validBookings = Set.of("KZ-9021", "FL-4410");

    public boolean isValid(String bookingRef) {
        return validBookings.contains(bookingRef);
    }
}

class SeatAllocator {
    private int nextRow = 12;

    public String allocateSeat() {
        return (nextRow++) + "A";
    }
}

class BaggageHandler {
    private static final double MAX_WEIGHT_KG = 23.0;

    public boolean checkAndTagBag(String bookingRef, double weightKg) {
        if (weightKg > MAX_WEIGHT_KG) {
            return false;
        }
        if (weightKg > 0) {
            System.out.println("[Baggage] Tagged bag (" + weightKg + " kg) for " + bookingRef);
        }
        return true;
    }
}

class BoardingPassGenerator {
    public String generate(String bookingRef, String seat, double bagWeight) {
        return "=================================\n" +
               "       BOARDING PASS             \n" +
               " Booking Ref : " + bookingRef + "\n" +
               " Seat        : " + seat + "\n" +
               " Checked Bag : " + bagWeight + " kg\n" +
               "=================================";
    }
}

class AirportCheckInFacade {
    private final TicketVerifier verifier;
    private final SeatAllocator seatAllocator;
    private final BaggageHandler baggageHandler;
    private final BoardingPassGenerator passGenerator;

    public AirportCheckInFacade(TicketVerifier verifier, SeatAllocator seatAllocator,
                                BaggageHandler baggageHandler, BoardingPassGenerator passGenerator) {
        this.verifier = verifier;
        this.seatAllocator = seatAllocator;
        this.baggageHandler = baggageHandler;
        this.passGenerator = passGenerator;
    }

    public String completeCheckIn(String bookingRef, double bagWeightKg) {
        if (!verifier.isValid(bookingRef)) {
            return "Check-in failed: Invalid booking reference " + bookingRef;
        }
        if (!baggageHandler.checkAndTagBag(bookingRef, bagWeightKg)) {
            return "Check-in failed: Bag exceeds 23.0 kg limit (" + bagWeightKg + " kg).";
        }
        String seat = seatAllocator.allocateSeat();
        return passGenerator.generate(bookingRef, seat, bagWeightKg);
    }
}

public class Main {
    public static void main(String[] args) {
        AirportCheckInFacade checkIn = new AirportCheckInFacade(
                new TicketVerifier(), new SeatAllocator(), new BaggageHandler(), new BoardingPassGenerator()
        );

        System.out.println(checkIn.completeCheckIn("KZ-9021", 26.5));
        System.out.println();
        System.out.println(checkIn.completeCheckIn("KZ-9021", 18.2));
    }
}