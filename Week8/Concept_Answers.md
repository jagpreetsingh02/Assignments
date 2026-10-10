# Week 8 Concept Questions

## 1. Invariants in a ShoppingCart

An invariant is a condition that must be true every time someone looks at the object. For a ShoppingCart, the invariant is `total_price == sum of all LineItem prices`.

If `total_price` were a public field, any code could write `cart.total_price = 0` while the items stayed in the cart, and the cart would now be lying. Encapsulation prevents this:

- `total_price` and the item list are private.
- The only way to change the cart is through methods like `addItem()`, `removeItem()` and `updateQuantity()`. Each one updates the items and recalculates the total in the same step. Another option is to compute the total on demand in `getTotal()` and not store it at all.
- There is no `setTotalPrice()`.

Every path that changes the cart goes through code that restores the invariant before returning, so the object can't be left in an invalid state. A method can also reject bad input, such as a negative quantity, before it changes anything.

## 2. Composition over inheritance for SmartDevice

With inheritance, every combination needs its own class: `SmartDeviceWithCamera`, `SmartDeviceWithGPS`, `SmartDeviceWithCameraAndGPS`, `SmartDeviceWithCameraAndGPSAndBluetooth`... With 3 optional features that's already 8 classes, and every new feature doubles the count. Java also doesn't allow extending more than one class, so you can't "mix" two of them.

With composition, the device *has* its capabilities:

```java
interface Capability { String describe(); }
class Camera implements Capability { ... }
class Gps implements Capability { ... }
class Bluetooth implements Capability { ... }

class SmartDevice {
    private final List<Capability> capabilities = new ArrayList<>();
    void add(Capability c) { capabilities.add(c); }
}
```

A phone with a camera and GPS is just `device.add(new Camera()); device.add(new Gps());`. A new capability like NFC is one new class, and nothing that already exists has to change. Capabilities can even be added or removed at runtime, which an inheritance hierarchy can't do. Each capability class can also be tested on its own.

## 3. Sequence diagram for checkout

Participants across the top, each with a dashed lifeline going down:
`CheckoutManager`, `PaymentGateway`, `BankService`.

Time runs top to bottom:

1. `CheckoutManager → PaymentGateway : processPayment(amount)` (solid arrow, synchronous call)
2. `PaymentGateway → BankService : authorize(card, amount)`
3. `BankService --> PaymentGateway : authResult` (dashed return arrow)
4. `PaymentGateway --> CheckoutManager : paymentStatus` (dashed return arrow)

Activation bars (thin rectangles on a lifeline) show when each object is busy. The PaymentGateway's bar stays open while it waits for the bank. Message order is read strictly from top to bottom, so it's clear that the bank is called after the gateway receives the request and that the status reaches the CheckoutManager last. A class diagram only shows that these classes are connected. The sequence diagram shows who calls whom, in what order, and where each result flows back, which makes it the right tool for understanding and debugging this kind of runtime collaboration.

## 4. Abstraction in a NotificationService

Define the contract once:

```java
interface Notifier { void send(String to, String message); }
class EmailNotifier implements Notifier { ... }
class SmsNotifier implements Notifier { ... }
class PushNotifier implements Notifier { ... }

class NotificationService {
    private final Notifier notifier;
    NotificationService(Notifier notifier) { this.notifier = notifier; }
    void notifyUser(String to, String msg) { notifier.send(to, msg); }
}
```

The client only talks to `NotificationService`, and `NotificationService` only knows the `Notifier` interface. Which concrete class runs is decided when the object is created, for example from configuration. To add WhatsApp, you write `WhatsAppNotifier implements Notifier` and pass it in. The client code and the service code are not edited or retested. This is dependency on abstractions: the high-level code depends on the interface, and the low-level details depend on that same interface. The design is open for extension and closed for modification.

## 5. Aggregation vs composition (University, Department, Professor)

Both are "whole-part" relationships. The difference is **ownership and lifetime**.

- **University ◆── Department is composition.** The university creates and owns its departments. A department doesn't make sense on its own, and when the university is closed its departments disappear with it. In UML this is a filled diamond on the University side, with multiplicity University 1 ── 1..* Department.
- **Department ◇── Professor is aggregation.** A department groups professors, but it doesn't own their existence. Professors exist before they join, and if the department or university shuts down they keep existing and can join another institution. In UML this is a hollow diamond, with multiplicity Department 0..* ── 0..* Professor, since a professor may hold joint appointments.

So the test is: "If the whole is destroyed, must the part be destroyed too?" If yes, it's composition. If the part can survive and move elsewhere, it's aggregation.
