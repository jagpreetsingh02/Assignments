# Vehicle Rental System

## Classes

| Class | Attributes | Operations |
|---|---|---|
| Vehicle (abstract) | name, available | calculateCharge(days), isAvailable() |
| StandardCar / LuxuryCar / Suv | DAILY_RATE (+ discount for SUV) | calculateCharge(days) |
| Customer | name | |
| Rental | customer, vehicle, days, charge, active | close(), isActive() |
| RentalService | fleet, rentals | addVehicle(), rent(), returnVehicle() |

## Relationships and multiplicity

- RentalService 1 ◇── 0..* Vehicle (aggregation, the fleet)
- RentalService 1 ◆── 0..* Rental
- Customer 1 ── 0..* Rental
- Vehicle 1 ── 0..* Rental, but at most **one active** rental at a time
- StandardCar, LuxuryCar, Suv ──▷ Vehicle

## Flow

1. `rent()` finds the vehicle and checks for an active rental on it. If one exists, the request is refused, which prevents double booking.
2. A `Rental` is created. It asks the vehicle for `calculateCharge(days)`, which runs the subclass's pricing rule (runtime polymorphism).
3. The vehicle is marked unavailable.
4. `returnVehicle()` closes the active rental and marks the vehicle available again.

Adding a new category (for example `ElectricCar`) only needs a new `Vehicle` subclass with its own `calculateCharge`. `RentalService` stays the same.
