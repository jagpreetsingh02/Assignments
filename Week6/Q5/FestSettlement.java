class EventTicket {
    private static int ticketsIssued = 0;

    public final String ticketId;
    private final double basePrice;
    private double amountPaid;

    public EventTicket(double basePrice) {
        ticketsIssued++;
        this.ticketId = "TCK-" + (1000 + ticketsIssued);
        this.basePrice = basePrice;
    }

    void pay(double amount) {
        if (amount <= 0) {
            System.out.println("Payment must be positive");
            return;
        }
        amountPaid += amount;
    }

    void pay(double amount, String mode) {
        System.out.println("Paying " + amount + " via " + mode + " for " + ticketId);
        pay(amount);
    }

    double getBalanceDue() {
        return basePrice - amountPaid;
    }

    static int getTicketsIssued() {
        return ticketsIssued;
    }

    static boolean isValidPromoCode(String code) {
        if (code == null || code.length() != 5) {
            return false;
        }
        if (code.charAt(0) != 'F') {
            return false;
        }
        for (int i = 1; i <= 3; i++) {
            if (!Character.isDigit(code.charAt(i))) {
                return false;
            }
        }
        return Character.isUpperCase(code.charAt(4));
    }

    static String processNightlySettlement(EventTicket[] tickets) {
        int processed = 0;
        int nullSkipped = 0;
        int group = 0;
        int individual = 0;

        if (tickets != null) {
            for (EventTicket t : tickets) {
                if (t == null) {
                    nullSkipped++;
                    continue;
                }
                if (t instanceof GroupTicket) {
                    group++;
                } else {
                    individual++;
                }
                processed++;
            }
        }

        return processed + " processed | " + nullSkipped + " null skipped | "
                + group + " group | " + individual + " individual";
    }
}

class GroupTicket extends EventTicket {
    private final int groupSize;

    public GroupTicket(double basePrice, int groupSize) {
        super(basePrice);
        if (groupSize <= 0) {
            throw new IllegalArgumentException("groupSize must be positive");
        }
        this.groupSize = groupSize;
    }

    int getGroupSize() {
        return groupSize;
    }
}

public class FestSettlement {
    public static void main(String[] args) {
        EventTicket t1 = new EventTicket(500);
        System.out.println(t1.ticketId);
        System.out.println(EventTicket.getTicketsIssued());

        System.out.println(EventTicket.isValidPromoCode("F123A"));
        System.out.println(EventTicket.isValidPromoCode("F12A"));
        System.out.println(EventTicket.isValidPromoCode("X123A"));

        t1.pay(200);
        t1.pay(200, "UPI");
        System.out.println(t1.getBalanceDue());

        EventTicket[] night = {new GroupTicket(2000, 5), null, new EventTicket(500)};
        System.out.println(EventTicket.processNightlySettlement(night));
        System.out.println("Tickets issued so far: " + EventTicket.getTicketsIssued());
    }
}
