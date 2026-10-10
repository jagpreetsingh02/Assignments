class FoodOrder {
    private final String studentName;
    private final String dishName;
    private boolean delivered;

    public FoodOrder(String studentName, String dishName) {
        if (isBlank(studentName)) {
            throw new IllegalArgumentException("student name is missing");
        }
        if (isBlank(dishName)) {
            throw new IllegalArgumentException("dish name is missing");
        }
        this.studentName = studentName.trim();
        this.dishName = dishName.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    void markDelivered() {
        if (delivered) {
            System.out.println("ALERT: " + dishName + " for " + studentName
                    + " was already delivered - check for a double-serve");
            return;
        }
        delivered = true;
        System.out.println(dishName + " delivered to " + studentName);
    }

    static void processBatch(String[][] rawOrders) {
        int valid = 0;
        int rejected = 0;

        for (int i = 0; i < rawOrders.length; i++) {
            String[] raw = rawOrders[i];
            String student = (raw != null && raw.length > 0) ? raw[0] : null;
            String dish = (raw != null && raw.length > 1) ? raw[1] : null;

            try {
                new FoodOrder(student, dish);
                valid++;
            } catch (IllegalArgumentException e) {
                rejected++;
                System.out.println("Entry " + (i + 1) + " rejected: " + e.getMessage());
            }
        }

        System.out.println("Valid: " + valid + " | Rejected: " + rejected);
    }
}

public class GhostOrderValidator {
    public static void main(String[] args) {
        String[][] raw = {
                {"Ravi", "Paneer Butter Masala"},
                {"", "Chole Bhature"},
                {"Meera", " "},
                {"Divya", "Veg Biryani"}
        };
        FoodOrder.processBatch(raw);

        System.out.println();
        FoodOrder order = new FoodOrder("Ravi", "Paneer Butter Masala");
        order.markDelivered();
        order.markDelivered();
    }
}
