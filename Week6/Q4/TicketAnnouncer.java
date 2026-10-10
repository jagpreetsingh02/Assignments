class EventTicket {
    private final double basePrice;
    private double amountPaid;

    public EventTicket(double basePrice) {
        this.basePrice = basePrice;
    }

    void pay(double amount) {
        if (amount > 0) {
            amountPaid += amount;
        }
    }

    double getBalanceDue() {
        return basePrice - amountPaid;
    }

    String printTicket() {
        return "Standard | Balance: " + getBalanceDue();
    }
}

class WorkshopTicket extends EventTicket {
    private final String track;

    public WorkshopTicket(double basePrice, String track) {
        super(basePrice);
        this.track = track;
    }

    String getTrack() {
        return track;
    }

    @Override
    String printTicket() {
        return "Workshop | Track: " + track + " | Balance: " + getBalanceDue();
    }
}

public class TicketAnnouncer {

    static String batchPrint(EventTicket[] tickets) {
        StringBuilder report = new StringBuilder();

        for (EventTicket t : tickets) {
            report.append(t.printTicket());
            if (t instanceof WorkshopTicket) {
                WorkshopTicket w = (WorkshopTicket) t;
                report.append(" [Track via downcast: ").append(w.getTrack()).append("]");
            }
            report.append(" | ");
        }
        return report.toString();
    }

    public static void main(String[] args) {
        EventTicket[] tickets = {
                new EventTicket(500),
                new WorkshopTicket(1200, "AI/ML")
        };
        System.out.println(batchPrint(tickets));

        EventTicket plain = new EventTicket(500);
        try {
            WorkshopTicket bad = (WorkshopTicket) plain;
            System.out.println(bad.getTrack());
        } catch (ClassCastException e) {
            System.out.println("ClassCastException at runtime: " + e.getMessage());
        }
    }
}
