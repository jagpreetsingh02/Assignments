abstract class LibraryItem {
    private static int counter = 1000;

    private final String itemId;
    protected final String title;

    protected LibraryItem(String title) {
        counter++;
        this.itemId = "LI-" + counter;
        this.title = title;
    }

    String getItemId() {
        return itemId;
    }

    public abstract int getLoanPeriodDays();
}

interface Renewable {
    String renew();
}

interface Reservable {
    String reserve();
}

class Textbook extends LibraryItem implements Renewable, Reservable {

    public Textbook(String title) {
        super(title);
    }

    @Override
    public int getLoanPeriodDays() {
        return 14;
    }

    @Override
    public String renew() {
        return title + " renewed";
    }

    @Override
    public String reserve() {
        return title + " reserved";
    }
}

class Magazine extends LibraryItem implements Renewable {

    public Magazine(String title) {
        super(title);
    }

    @Override
    public int getLoanPeriodDays() {
        return 7;
    }

    @Override
    public String renew() {
        return title + " renewed";
    }
}

class DigitalPass implements Renewable {
    private final String resourceName;

    public DigitalPass(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String renew() {
        return resourceName + " access renewed for 30 more days";
    }
}

public class LibraryCheckout {

    static void processCheckouts(LibraryItem[] items) {
        for (LibraryItem item : items) {
            System.out.println(item.getItemId() + " -> loan period " + item.getLoanPeriodDays() + " days");
        }
    }

    static String reserveIfSupported(Object o) {
        if (o instanceof Reservable) {
            return ((Reservable) o).reserve();
        }
        return "Reservation not supported";
    }

    public static void main(String[] args) {
        Textbook t = new Textbook("Java Fundamentals");
        System.out.println(t.getLoanPeriodDays());
        System.out.println(t.renew());
        System.out.println(t.reserve());

        Magazine m = new Magazine("Tech Monthly");
        System.out.println(reserveIfSupported(m));

        DigitalPass d = new DigitalPass("E-Journal Access");
        System.out.println(reserveIfSupported(d));
        System.out.println(d.renew());

        // Upcasting: a Textbook stored in a LibraryItem variable.
        LibraryItem ref = t;
        System.out.println(reserveIfSupported(ref));

        System.out.println();
        processCheckouts(new LibraryItem[]{t, m});
    }
}
