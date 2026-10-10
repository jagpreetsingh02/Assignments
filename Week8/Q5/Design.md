# Food Order and Flexible Payment System

## Classes

| Class | Attributes | Operations |
|---|---|---|
| Customer (implements OrderEventListener) | name, orders | createOrder(), onEvent(msg) |
| Restaurant | name, menu | addToMenu(), find(name) |
| FoodItem | name, price | |
| LineItem | item, quantity | subtotal() |
| Order | orderNumber, items, listeners, status | addItem(), total(), place(payment), pay(payment) |
| OrderStatus (enum) | CREATED, PENDING_PAYMENT, PAID | |
| PaymentMethod (interface) | | name(), pay(amount) |
| CreditCardPayment / DigitalWalletPayment / CashOnDelivery | limit / balance / none | pay(amount) |
| OrderEventListener (interface) | | onEvent(msg) |

## Relationships and multiplicity

- Customer 1 ── 0..* Order
- Order 1 ◆── 1..* LineItem (composition, line items die with the order)
- LineItem * ── 1 FoodItem
- Restaurant 1 ◇── 0..* FoodItem
- Order ┄┄> PaymentMethod (dependency, passed in at payment time)
- Order 1 ── 1..* OrderEventListener
- CreditCardPayment, DigitalWalletPayment, CashOnDelivery ┄┄▷ PaymentMethod
- Customer ┄┄▷ OrderEventListener

## Flow

1. `Customer.createOrder()` builds an `Order` and registers the customer as its listener.
2. `addItem()` wraps each `FoodItem` and quantity in a `LineItem` owned by the order.
3. `place(payment)` refuses an empty order. Otherwise it gives the order a number and moves it to PENDING_PAYMENT.
4. `pay(payment)` calls `payment.pay(total)`. The order only knows the `PaymentMethod` interface.
5. On success the status becomes PAID. On failure it stays PENDING_PAYMENT and the customer can pay again with another method.
6. In both cases the order notifies its listeners, so the customer gets the event.

A new payment option (for example UPI) is one new class implementing `PaymentMethod`. `Order` stays the same.
