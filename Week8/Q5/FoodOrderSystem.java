import java.util.ArrayList;
import java.util.List;

class FoodItem {
    private final String name;
    private final double price;

    FoodItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    String getName() {
        return name;
    }

    double getPrice() {
        return price;
    }
}

class Restaurant {
    private final String name;
    private final List<FoodItem> menu = new ArrayList<>();

    Restaurant(String name) {
        this.name = name;
    }

    void addToMenu(FoodItem item) {
        menu.add(item);
    }

    FoodItem find(String itemName) {
        for (FoodItem f : menu) {
            if (f.getName().equalsIgnoreCase(itemName)) {
                return f;
            }
        }
        return null;
    }
}

class LineItem {
    private final FoodItem item;
    private final int quantity;

    LineItem(FoodItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    double subtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return item.getName() + " (Qty " + quantity + ")";
    }
}

interface PaymentMethod {
    String name();

    boolean pay(double amount);
}

class CreditCardPayment implements PaymentMethod {
    private final double creditLimit;

    CreditCardPayment(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    @Override
    public String name() {
        return "Credit Card";
    }

    @Override
    public boolean pay(double amount) {
        return amount <= creditLimit;
    }
}

class DigitalWalletPayment implements PaymentMethod {
    private double balance;

    DigitalWalletPayment(double balance) {
        this.balance = balance;
    }

    @Override
    public String name() {
        return "Digital Wallet";
    }

    @Override
    public boolean pay(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
}

class CashOnDelivery implements PaymentMethod {
    @Override
    public String name() {
        return "Cash on Delivery";
    }

    @Override
    public boolean pay(double amount) {
        return true;
    }
}

interface OrderEventListener {
    void onEvent(String message);
}

enum OrderStatus {
    CREATED("Created"), PENDING_PAYMENT("Pending Payment"), PAID("Paid");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    String label() {
        return label;
    }
}

class Order {
    private static int nextOrderNumber = 123;

    private int orderNumber;
    private final List<LineItem> items = new ArrayList<>();
    private final List<OrderEventListener> listeners = new ArrayList<>();
    private OrderStatus status = OrderStatus.CREATED;

    Order(OrderEventListener listener) {
        listeners.add(listener);
    }

    void addItem(FoodItem item, int quantity) {
        if (status != OrderStatus.CREATED) {
            System.out.println("Cannot modify an order that is already placed.");
            return;
        }
        if (item == null || quantity <= 0) {
            System.out.println("Invalid item or quantity.");
            return;
        }
        items.add(new LineItem(item, quantity));
    }

    String describeItems() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(items.get(i));
        }
        return sb.toString();
    }

    double total() {
        double sum = 0;
        for (LineItem li : items) {
            sum += li.subtotal();
        }
        return sum;
    }

    boolean place(PaymentMethod payment) {
        if (items.isEmpty()) {
            System.out.println("Cannot place order: Order must contain at least one item.");
            return false;
        }
        if (status == OrderStatus.CREATED) {
            orderNumber = nextOrderNumber++;
            status = OrderStatus.PENDING_PAYMENT;
            System.out.println("Order placed.");
        }
        return pay(payment);
    }

    boolean pay(PaymentMethod payment) {
        if (status != OrderStatus.PENDING_PAYMENT) {
            System.out.println("Order #" + orderNumber + " is not awaiting payment.");
            return false;
        }

        boolean ok = payment.pay(total());
        if (ok) {
            status = OrderStatus.PAID;
            System.out.println("Payment via " + payment.name() + " successful. Order status: " + status.label() + ".");
            notifyListeners("Order #" + orderNumber + " placed and paid.");
        } else {
            System.out.println("Payment via " + payment.name() + " failed. Order status: " + status.label() + ".");
            notifyListeners("Order #" + orderNumber + " placed, awaiting payment.");
        }
        return ok;
    }

    private void notifyListeners(String message) {
        for (OrderEventListener l : listeners) {
            l.onEvent(message);
        }
    }
}

class Customer implements OrderEventListener {
    private final String name;
    private final List<Order> orders = new ArrayList<>();

    Customer(String name) {
        this.name = name;
    }

    Order createOrder() {
        Order order = new Order(this);
        orders.add(order);
        System.out.println("Order created.");
        return order;
    }

    @Override
    public void onEvent(String message) {
        System.out.println("Notification: " + message);
    }
}

public class FoodOrderSystem {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant("Campus Bites");
        restaurant.addToMenu(new FoodItem("Pizza", 250));
        restaurant.addToMenu(new FoodItem("Soda", 40));
        restaurant.addToMenu(new FoodItem("Burger", 180));

        Customer customer = new Customer("Rhea");

        Order first = customer.createOrder();
        first.addItem(restaurant.find("Pizza"), 2);
        first.addItem(restaurant.find("Soda"), 1);
        System.out.println("Added " + first.describeItems() + ".");

        Order empty = customer.createOrder();
        empty.place(new CashOnDelivery());

        first.place(new CreditCardPayment(5000));

        System.out.println();
        Order second = customer.createOrder();
        second.addItem(restaurant.find("Burger"), 1);
        System.out.println("Added " + second.describeItems() + ".");
        second.place(new DigitalWalletPayment(100));

        System.out.println();
        second.pay(new CashOnDelivery());
    }
}
