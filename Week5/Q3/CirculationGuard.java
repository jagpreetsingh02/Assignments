class BookInventory {
    private final int copiesTotal;
    private int copiesAvailable;

    BookInventory(int copiesTotal) {
        if (copiesTotal <= 0) {
            throw new IllegalArgumentException("copiesTotal must be positive");
        }
        this.copiesTotal = copiesTotal;
        this.copiesAvailable = copiesTotal;
    }

    void checkOut() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
        }
    }

    void checkIn() {
        if (copiesAvailable < copiesTotal) {
            copiesAvailable++;
        }
    }

    int getCopiesAvailable() {
        return copiesAvailable;
    }

    int getCopiesTotal() {
        return copiesTotal;
    }
}

public class CirculationGuard {
    public static void main(String[] args) {
        try {
            new BookInventory(0);
        } catch (IllegalArgumentException e) {
            System.out.println("new BookInventory(0) -> construction rejected");
        }

        BookInventory b = new BookInventory(3);
        b.checkOut();
        b.checkOut();
        b.checkOut();
        b.checkOut();
        System.out.println("After 4 checkouts: " + b.getCopiesAvailable());

        b.checkIn();
        b.checkIn();
        b.checkIn();
        b.checkIn();
        System.out.println("After 4 check-ins: " + b.getCopiesAvailable());
    }
}
