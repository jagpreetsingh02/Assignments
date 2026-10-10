import java.util.ArrayList;
import java.util.List;

abstract class Vehicle {
    private final String name;
    private boolean available = true;

    protected Vehicle(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    boolean isAvailable() {
        return available;
    }

    void setAvailable(boolean available) {
        this.available = available;
    }

    abstract double calculateCharge(int days);
}

class StandardCar extends Vehicle {
    private static final double DAILY_RATE = 50.0;

    StandardCar(String name) {
        super(name);
    }

    @Override
    double calculateCharge(int days) {
        return DAILY_RATE * days;
    }
}

class LuxuryCar extends Vehicle {
    private static final double DAILY_RATE = 100.0;

    LuxuryCar(String name) {
        super(name);
    }

    @Override
    double calculateCharge(int days) {
        return DAILY_RATE * days;
    }
}

class Suv extends Vehicle {
    private static final double DAILY_RATE = 75.0;
    private static final double LONG_RENTAL_DISCOUNT = 0.10;

    Suv(String name) {
        super(name);
    }

    @Override
    double calculateCharge(int days) {
        double total = DAILY_RATE * days;
        return days >= 7 ? total * (1 - LONG_RENTAL_DISCOUNT) : total;
    }
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

class Rental {
    private final Customer customer;
    private final Vehicle vehicle;
    private final int days;
    private final double charge;
    private boolean active = true;

    Rental(Customer customer, Vehicle vehicle, int days) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.charge = vehicle.calculateCharge(days);
    }

    void close() {
        active = false;
    }

    boolean isActive() {
        return active;
    }

    Vehicle getVehicle() {
        return vehicle;
    }

    double getCharge() {
        return charge;
    }

    int getDays() {
        return days;
    }
}

class RentalService {
    private final List<Vehicle> fleet = new ArrayList<>();
    private final List<Rental> rentals = new ArrayList<>();

    void addVehicle(Vehicle v) {
        fleet.add(v);
    }

    Rental rent(Customer customer, String vehicleName, int days) {
        Vehicle vehicle = findVehicle(vehicleName);
        if (vehicle == null) {
            System.out.println(vehicleName + " is not part of the fleet.");
            return null;
        }
        if (days <= 0) {
            System.out.println("Rental duration must be at least 1 day.");
            return null;
        }
        if (findActiveRental(vehicle) != null) {
            System.out.println(vehicleName + " is already rented. Please choose another vehicle.");
            return null;
        }

        Rental rental = new Rental(customer, vehicle, days);
        rentals.add(rental);
        vehicle.setAvailable(false);
        System.out.printf("%s rented for %d days. Total charge: $%.2f%n", vehicleName, days, rental.getCharge());
        return rental;
    }

    void returnVehicle(String vehicleName) {
        Vehicle vehicle = findVehicle(vehicleName);
        Rental rental = vehicle == null ? null : findActiveRental(vehicle);
        if (rental == null) {
            System.out.println(vehicleName + " has no active rental.");
            return;
        }
        rental.close();
        vehicle.setAvailable(true);
        System.out.println(vehicleName + " returned. Now available.");
    }

    private Vehicle findVehicle(String name) {
        for (Vehicle v : fleet) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    private Rental findActiveRental(Vehicle vehicle) {
        for (Rental r : rentals) {
            if (r.isActive() && r.getVehicle() == vehicle) {
                return r;
            }
        }
        return null;
    }
}

public class VehicleRentalSystem {
    public static void main(String[] args) {
        RentalService service = new RentalService();
        service.addVehicle(new LuxuryCar("Luxury Car A"));
        service.addVehicle(new StandardCar("Standard Car B"));
        service.addVehicle(new Suv("SUV C"));

        Customer customer = new Customer("Arjun");
        Customer other = new Customer("Neha");

        service.rent(customer, "Luxury Car A", 3);
        service.rent(customer, "Standard Car B", 5);
        service.rent(other, "Luxury Car A", 2);
        service.returnVehicle("Luxury Car A");
        service.rent(other, "Luxury Car A", 2);
        service.rent(other, "SUV C", 7);
    }
}
