import java.util.Arrays;

class EventTicket {
    private static final int MAX_LATE_FEES = 10;

    private final double basePrice;
    private double amountPaid;
    private double lateFees;
    private final double[] lateFeeHistory = new double[MAX_LATE_FEES];
    private int lateFeeCount;

    public EventTicket(double basePrice) {
        this.basePrice = basePrice;
    }

    void pay(double amount) {
        if (amount > 0) {
            amountPaid += amount;
        }
    }

    double getBalanceDue() {
        return basePrice + lateFees - amountPaid;
    }

    protected void applyLateFee(double amount) {
        if (lateFeeCount == MAX_LATE_FEES) {
            System.out.println("Late fee limit reached, penalty not applied");
            return;
        }
        lateFees += amount;
        lateFeeHistory[lateFeeCount++] = amount;
    }

    double[] getLateFeeHistory() {
        return Arrays.copyOf(lateFeeHistory, lateFeeCount);
    }
}

class WorkshopTicket extends EventTicket {

    public WorkshopTicket(double basePrice) {
        super(basePrice);
    }

    @Override
    protected void applyLateFee(double amount) {
        super.applyLateFee(amount * 2);
    }
}

public class LateFeeAudit {
    public static void main(String[] args) {
        WorkshopTicket w = new WorkshopTicket(1200);
        w.pay(1200);
        w.applyLateFee(100);
        System.out.println("Balance due: " + w.getBalanceDue());

        double[] history = w.getLateFeeHistory();
        System.out.println("History: " + Arrays.toString(history));
        history[0] = 999;
        System.out.println("History after tampering: " + Arrays.toString(w.getLateFeeHistory()));

        EventTicket plain = new EventTicket(500);
        plain.applyLateFee(100);
        System.out.println("Standard ticket balance: " + plain.getBalanceDue()
                + " | History: " + Arrays.toString(plain.getLateFeeHistory()));
    }
}
