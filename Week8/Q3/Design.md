# Hotel Booking and Cancellation System

## Classes

| Class | Attributes | Operations |
|---|---|---|
| Customer | name | |
| Room | number, category, reservations | isAvailable(in, out), addReservation() |
| RoomCategory (enum) | label, pricing | getPricing() |
| PricingStrategy (interface) | | calculatePrice(nights) |
| StandardPricing / DeluxePricing / SuitePricing | rates | calculatePrice(nights) |
| Reservation | customer, room, checkIn, checkOut, totalPrice, status | overlaps(), cancel(today), getCancellationDeadline() |
| BookingManager | rooms | addRoom(), book() |

## Relationships and multiplicity

- BookingManager 1 ◆── 1..* Room
- Room 1 ── 0..* Reservation (only non-overlapping ones can be ACTIVE)
- Customer 1 ── 0..* Reservation
- Room * ── 1 RoomCategory, RoomCategory 1 ── 1 PricingStrategy
- StandardPricing, DeluxePricing, SuitePricing ┄┄▷ PricingStrategy (realization)

## Rules

- **Overlap:** two stays overlap when `start1 < end2 && start2 < end1`. Checking out on the 5th and someone else checking in on the 5th is allowed.
- **Price:** the number of nights is passed to the category's `PricingStrategy`. Deluxe is $200/night, so 4 nights cost $800. Standard is $150/night, so 4 nights cost $600.
- **Cancellation:** allowed only up to 2 days before check-in. After that, `cancel()` refuses.

## Flow

1. `BookingManager.book()` finds the room and asks `room.isAvailable(in, out)`.
2. The room checks every ACTIVE reservation for an overlap.
3. If the room is free, a `Reservation` is created. Its price comes from the category's strategy.
4. `Reservation.cancel(today)` compares today with the deadline and either cancels or refuses. A cancelled reservation no longer blocks the room.
