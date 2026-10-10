import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

interface PricingStrategy {
    double calculatePrice(long nights);
}

class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(long nights) {
        return 150.0 * nights;
    }
}

class DeluxePricing implements PricingStrategy {
    @Override
    public double calculatePrice(long nights) {
        return 200.0 * nights;
    }
}

class SuitePricing implements PricingStrategy {
    private static final double SERVICE_FEE = 100.0;

    @Override
    public double calculatePrice(long nights) {
        return 400.0 * nights + SERVICE_FEE;
    }
}

enum RoomCategory {
    STANDARD("Standard", new StandardPricing()),
    DELUXE("Deluxe", new DeluxePricing()),
    SUITE("Suite", new SuitePricing());

    private final String label;
    private final PricingStrategy pricing;

    RoomCategory(String label, PricingStrategy pricing) {
        this.label = label;
        this.pricing = pricing;
    }

    String getLabel() {
        return label;
    }

    PricingStrategy getPricing() {
        return pricing;
    }
}

enum ReservationStatus {
    ACTIVE, CANCELLED
}

class Customer {
    private final String name;

    Customer(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Room {
    private final int number;
    private final RoomCategory category;
    private final List<Reservation> reservations = new ArrayList<>();

    Room(int number, RoomCategory category) {
        this.number = number;
        this.category = category;
    }

    boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
        for (Reservation r : reservations) {
            if (r.isActive() && r.overlaps(checkIn, checkOut)) {
                return false;
            }
        }
        return true;
    }

    void addReservation(Reservation r) {
        reservations.add(r);
    }

    int getNumber() {
        return number;
    }

    RoomCategory getCategory() {
        return category;
    }

    String getDisplayName() {
        return category.getLabel() + " Room " + number;
    }
}

class Reservation {
    private static final int CANCELLATION_DAYS_BEFORE = 2;

    private final Customer customer;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalPrice;
    private ReservationStatus status = ReservationStatus.ACTIVE;

    Reservation(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.customer = customer;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        this.totalPrice = room.getCategory().getPricing().calculatePrice(nights);
    }

    boolean overlaps(LocalDate otherIn, LocalDate otherOut) {
        return checkIn.isBefore(otherOut) && otherIn.isBefore(checkOut);
    }

    LocalDate getCancellationDeadline() {
        return checkIn.minusDays(CANCELLATION_DAYS_BEFORE);
    }

    boolean cancel(LocalDate today) {
        if (status == ReservationStatus.CANCELLED) {
            System.out.println("Reservation for " + room.getDisplayName() + " is already cancelled.");
            return false;
        }
        if (today.isAfter(getCancellationDeadline())) {
            System.out.println("Cancellation failed: deadline for " + room.getDisplayName()
                    + " was " + getCancellationDeadline() + ".");
            return false;
        }
        status = ReservationStatus.CANCELLED;
        System.out.println("Reservation for " + room.getDisplayName() + " cancelled successfully.");
        return true;
    }

    boolean isActive() {
        return status == ReservationStatus.ACTIVE;
    }

    double getTotalPrice() {
        return totalPrice;
    }
}

class BookingManager {
    private final List<Room> rooms = new ArrayList<>();

    void addRoom(Room room) {
        rooms.add(room);
    }

    Reservation book(Customer customer, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        Room room = findRoom(roomNumber);
        if (room == null) {
            System.out.println("Booking failed: room " + roomNumber + " does not exist.");
            return null;
        }
        if (!checkIn.isBefore(checkOut)) {
            System.out.println("Booking failed: check-out must be after check-in.");
            return null;
        }
        if (!room.isAvailable(checkIn, checkOut)) {
            System.out.println("Booking failed: " + room.getDisplayName() + " is not available for "
                    + checkIn + " to " + checkOut + ".");
            return null;
        }

        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        room.addReservation(reservation);
        System.out.printf("%s booked from %s to %s. Total price: $%.2f%n",
                room.getDisplayName(), checkIn, checkOut, reservation.getTotalPrice());
        return reservation;
    }

    private Room findRoom(int number) {
        for (Room r : rooms) {
            if (r.getNumber() == number) {
                return r;
            }
        }
        return null;
    }
}

public class HotelBookingSystem {
    public static void main(String[] args) {
        BookingManager hotel = new BookingManager();
        hotel.addRoom(new Room(101, RoomCategory.DELUXE));
        hotel.addRoom(new Room(205, RoomCategory.STANDARD));
        hotel.addRoom(new Room(301, RoomCategory.SUITE));

        Customer customer = new Customer("Ananya");

        Reservation deluxe = hotel.book(customer, 101, LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 5));
        Reservation standard = hotel.book(customer, 205, LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 7));
        hotel.book(customer, 101, LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 7));
        deluxe.cancel(LocalDate.of(2024, 11, 25));

        System.out.println();
        hotel.book(customer, 101, LocalDate.of(2024, 12, 3), LocalDate.of(2024, 12, 7));
        standard.cancel(LocalDate.of(2024, 12, 2));
        hotel.book(customer, 301, LocalDate.of(2024, 12, 10), LocalDate.of(2024, 12, 12));
    }
}
