package task10;

import java.util.*;

class RoomAvailability {
    private final Map<String, Queue<Integer>> freeRooms = new HashMap<>();
    private final Map<Integer, String> occupiedRooms = new HashMap<>();
    private final Map<String, Double> rates = Map.of(
            "SINGLE", 80.0,
            "DOUBLE", 130.0,
            "SUITE", 250.0
    );

    public RoomAvailability() {
        freeRooms.put("SINGLE", new LinkedList<>(List.of(101, 102)));
        freeRooms.put("DOUBLE", new LinkedList<>(List.of(201)));
        freeRooms.put("SUITE", new LinkedList<>(List.of(301)));
    }

    public Integer assignRoom(String type) {
        Queue<Integer> queue = freeRooms.get(type.toUpperCase());
        if (queue == null || queue.isEmpty()) return null;
        int roomNum = queue.poll();
        occupiedRooms.put(roomNum, type.toUpperCase());
        return roomNum;
    }

    public double getRate(String type) {
        return rates.getOrDefault(type.toUpperCase(), 0.0);
    }

    public void releaseRoom(int roomNumber) {
        String type = occupiedRooms.remove(roomNumber);
        if (type != null) {
            freeRooms.get(type).offer(roomNumber);
        }
    }
}

class BillingService {
    public void chargeStay(String guestName, double total) {
        System.out.printf("[Billing] Charged $%.2f to %s.%n", total, guestName);
    }
}

class KeyCardEncoder {
    public void encodeCard(int roomNumber, String guestName) {
        System.out.println("[KeyCard] Encoded access card for Room #" + roomNumber + " (" + guestName + ").");
    }
}

class HousekeepingNotifier {
    public void alertCleaning(int roomNumber) {
        System.out.println("[Housekeeping] Room #" + roomNumber + " is vacant and needs cleaning.");
    }
}

class HotelReceptionFacade {
    private final RoomAvailability rooms;
    private final BillingService billing;
    private final KeyCardEncoder keyEncoder;
    private final HousekeepingNotifier housekeeping;

    public HotelReceptionFacade(RoomAvailability rooms, BillingService billing, KeyCardEncoder keyEncoder, HousekeepingNotifier housekeeping) {
        this.rooms = rooms;
        this.billing = billing;
        this.keyEncoder = keyEncoder;
        this.housekeeping = housekeeping;
    }

    public int checkInGuest(String guestName, String roomType, int nights) {
        Integer roomNum = rooms.assignRoom(roomType);
        if (roomNum == null) {
            System.out.println("Sorry " + guestName + ", no " + roomType + " rooms available.");
            return -1;
        }
        double total = rooms.getRate(roomType) * nights;
        billing.chargeStay(guestName, total);
        keyEncoder.encodeCard(roomNum, guestName);
        System.out.println(guestName + " checked into Room #" + roomNum + ".\n");
        return roomNum;
    }

    public void checkOutGuest(int roomNumber) {
        rooms.releaseRoom(roomNumber);
        housekeeping.alertCleaning(roomNumber);
        System.out.println("Check-out complete for Room #" + roomNumber + ".\n");
    }
}

public class Main {
    public static void main(String[] args) {
        HotelReceptionFacade reception = new HotelReceptionFacade(
                new RoomAvailability(), new BillingService(), new KeyCardEncoder(), new HousekeepingNotifier()
        );

        int room1 = reception.checkInGuest("Alice", "DOUBLE", 3);
        reception.checkInGuest("Bob", "DOUBLE", 2); 
        if (room1 != -1) {
            reception.checkOutGuest(room1);
        }
        reception.checkInGuest("Bob", "DOUBLE", 2); 
    }
}