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
        if (amount > 0) {
            amountPaid += amount;
        }
    }

    double getBalanceDue() {
        return basePrice - amountPaid;
    }

    void printTicket() {
        System.out.println("Standard Event Ticket | Balance Due: " + getBalanceDue());
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

    @Override
    void printTicket() {
        System.out.println("Workshop Ticket | Track: " + track + " | Balance Due: " + getBalanceDue());
    }
}

class PremiumWorkshopTicket extends WorkshopTicket {
    private final double kitFee;

    public PremiumWorkshopTicket(String attendeeId, double basePrice, String track, double kitFee) {
        super(attendeeId, basePrice, track);
        this.kitFee = kitFee;
    }

    @Override
    void printTicket() {
        System.out.println("Premium Workshop Ticket | Track: " + getTrack()
                + " | Kit Fee: " + kitFee + " | Balance Due: " + getBalanceDue());
    }
}

class HackathonTicket extends EventTicket {
    private final String teamName;

    public HackathonTicket(String attendeeId, double basePrice, String teamName) {
        super(attendeeId, basePrice);
        this.teamName = teamName;
    }

    @Override
    void printTicket() {
        System.out.println("Hackathon Ticket | Team: " + teamName + " | Balance Due: " + getBalanceDue());
    }
}

public class TicketFamilyTree {

    static String classifyGeneration(EventTicket ticket) {
        if (ticket instanceof PremiumWorkshopTicket) {
            return "Multilevel descendant (3 generations deep)";
        } else if (ticket instanceof HackathonTicket) {
            return "Hierarchical sibling (independent branch)";
        } else if (ticket instanceof WorkshopTicket) {
            return "Single inheritance child (2 generations deep)";
        }
        return "Base ticket (root of the hierarchy)";
    }

    static double getTotalBalanceDue(EventTicket[] tickets) {
        double total = 0;
        for (EventTicket t : tickets) {
            total += t.getBalanceDue();
        }
        return total;
    }

    public static void main(String[] args) {
        EventTicket standard = new EventTicket("STU1", 500);
        EventTicket workshop = new WorkshopTicket("STU2", 1200, "AI/ML");
        EventTicket premium = new PremiumWorkshopTicket("STU3", 2000, "Cloud Native", 300);
        EventTicket hackathon = new HackathonTicket("STU4", 800, "Byte Force");

        EventTicket[] all = {standard, workshop, premium, hackathon};
        for (EventTicket t : all) {
            t.printTicket();
        }

        System.out.println();
        for (EventTicket t : all) {
            System.out.println(classifyGeneration(t));
        }

        System.out.println();
        System.out.println("Total balance due: " + getTotalBalanceDue(all));
    }
}
