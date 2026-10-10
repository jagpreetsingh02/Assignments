class ParkingSlot {
    String slotNo;
    int capacity;
    int occupiedCount;

    ParkingSlot(String slotNo, int capacity, int occupiedCount) {
        this.slotNo = slotNo;
        this.capacity = capacity;
        this.occupiedCount = occupiedCount;
    }

    boolean allot(String vehicleNo) {
        if (occupiedCount < capacity) {
            occupiedCount++;
            return true;
        }
        return false;
    }

    // The array holds references to ParkingSlot objects, and Java passes that array
    // reference by value. The method receives a copy of the reference, not copies of
    // the slots, so allot() changes the same objects the caller is holding.
    static ParkingSlot findAvailableSlot(ParkingSlot[] slots) {
        if (slots == null) {
            return null;
        }
        for (ParkingSlot slot : slots) {
            if (slot != null && slot.occupiedCount < slot.capacity) {
                return slot;
            }
        }
        return null;
    }

    static void safeAllot(ParkingSlot[] slots, String vehicleNo) {
        ParkingSlot slot = findAvailableSlot(slots);
        if (slot == null) {
            System.out.println("No slots available for " + vehicleNo);
            return;
        }
        slot.allot(vehicleNo);
        System.out.println(vehicleNo + " allotted to slot " + slot.slotNo);
    }
}

public class ParkingAllocation {
    public static void main(String[] args) {
        ParkingSlot[] withSpace = {
                new ParkingSlot("A1", 4, 3),
                new ParkingSlot("A2", 5, 5)
        };
        ParkingSlot.safeAllot(withSpace, "TN09AB1234");

        ParkingSlot[] allFull = {
                new ParkingSlot("A1", 4, 4),
                new ParkingSlot("A2", 5, 5)
        };
        ParkingSlot.safeAllot(allFull, "TN09AB1234");

        ParkingSlot.safeAllot(null, "TN10CD5678");
    }
}
