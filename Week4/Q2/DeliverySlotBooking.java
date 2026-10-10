class DeliverySlot {
    private static final String[] PEAK_SLOTS = {"12:00-13:00", "13:00-14:00", "19:00-20:00", "20:00-21:00"};

    private final String orderId;
    private final String timeSlot;

    public DeliverySlot(String orderId, String timeSlot) {
        this.orderId = orderId;
        this.timeSlot = timeSlot;
    }

    public DeliverySlot(String orderId) {
        this(orderId, "ASAP");
    }

    boolean isPeakHour() {
        for (String peak : PEAK_SLOTS) {
            if (peak.equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }

    String getOrderId() {
        return orderId;
    }

    String getTimeSlot() {
        return timeSlot;
    }
}

public class DeliverySlotBooking {
    public static void main(String[] args) {
        DeliverySlot[] bookings = {
                new DeliverySlot("ORD101", "13:00-14:00"),
                new DeliverySlot("ORD102"),
                new DeliverySlot("ORD103", "16:00-17:00"),
                new DeliverySlot("ORD104", "20:00-21:00")
        };

        for (DeliverySlot slot : bookings) {
            System.out.println(slot.getOrderId() + " [" + slot.getTimeSlot() + "] peak: " + slot.isPeakHour());
        }
    }
}
