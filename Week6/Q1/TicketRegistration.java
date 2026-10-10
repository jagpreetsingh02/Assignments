class EventTicket {
    private final String attendeeId;
    private final double basePrice;
    private double amountPaid;

    public EventTicket(String attendeeId, double basePrice) {
        if (attendeeId == null || attendeeId.trim().length() < 4) {
            throw new IllegalArgumentException("attendeeId must be at least 4 non-blank characters");
        }
        this.attendeeId = attendeeId.trim();
        this.basePrice = basePrice;
    }

    void pay(double amount) {
        if (amount <= 0) {
            System.out.println("Payment must be positive");
            return;
        }
        amountPaid += amount;
    }

    double getBalanceDue() {
        return basePrice - amountPaid;
    }

    String getAttendeeId() {
        return attendeeId;
    }

    double getBasePrice() {
        return basePrice;
    }

    static String registerBatch(String[] attendeeIds, double basePrice) {
        int registered = 0;
        int rejected = 0;

        for (String id : attendeeIds) {
            try {
                new EventTicket(id, basePrice);
                registered++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }
        return "Registered: " + registered + " | Rejected: " + rejected;
    }
}

class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(String attendeeId, double basePrice, String track) {
        super(attendeeId, basePrice);
        this.track = track;
    }

    String getTrack() {
        return track;
    }
}

public class TicketRegistration {
    public static void main(String[] args) {
        try {
            new EventTicket("ST1", 500);
        } catch (IllegalArgumentException e) {
            System.out.println("new EventTicket(\"ST1\", 500) -> construction rejected");
        }

        WorkshopTicket w = new WorkshopTicket("STU2", 1200, "AI/ML");
        w.pay(500);
        System.out.println("Workshop balance due: " + w.getBalanceDue());

        System.out.println(EventTicket.registerBatch(new String[]{"STU1", "ST1", "STU2", " ", "STU3"}, 500));
    }
}
