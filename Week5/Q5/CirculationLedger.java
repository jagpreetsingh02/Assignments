// LoanReceipt cannot be plain "final" because ReferenceOnlyLoanReceipt has to extend it.
// "sealed ... permits" closes it to every other class, and the one permitted subclass is final.
sealed class LoanReceipt permits ReferenceOnlyLoanReceipt {
    private static final String ID_PREFIX;
    private static final int ID_DIGITS;

    static {
        ID_PREFIX = "BK-";
        ID_DIGITS = 3;
    }

    private final String memberId;
    private final String[] bookIds;

    public LoanReceipt(String memberId, String[] bookIds) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("memberId is required");
        }
        if (bookIds == null || bookIds.length == 0) {
            throw new IllegalArgumentException("at least one book ID is required");
        }

        String[] copy = bookIds.clone();
        for (String id : copy) {
            if (!isValidBookId(id)) {
                throw new IllegalArgumentException("invalid book ID: " + id);
            }
        }
        this.memberId = memberId.trim();
        this.bookIds = copy;
    }

    static boolean isValidBookId(String id) {
        if (id == null || id.length() != ID_PREFIX.length() + ID_DIGITS || !id.startsWith(ID_PREFIX)) {
            return false;
        }
        for (int i = ID_PREFIX.length(); i < id.length(); i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public String getMemberId() {
        return memberId;
    }

    public String[] getBookIds() {
        return bookIds.clone();
    }

    public LoanReceipt withCorrectedBookId(int index, String newId) {
        return new LoanReceipt(memberId, replaced(index, newId));
    }

    protected String[] replaced(int index, String newId) {
        if (index < 0 || index >= bookIds.length) {
            throw new IllegalArgumentException("no book at index " + index);
        }
        String[] updated = bookIds.clone();
        updated[index] = newId;
        return updated;
    }

    static String processNightlyCirculation(LoanReceipt[] receipts) {
        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;

        if (receipts != null) {
            for (LoanReceipt r : receipts) {
                if (r == null) {
                    nullSkipped++;
                    continue;
                }
                if (r instanceof ReferenceOnlyLoanReceipt) {
                    referenceOnly++;
                } else {
                    regular++;
                }
                processed++;
            }
        }

        return processed + " processed | " + nullSkipped + " null skipped | "
                + referenceOnly + " reference-only | " + regular + " regular";
    }
}

final class ReferenceOnlyLoanReceipt extends LoanReceipt {
    private final String roomNumber;

    public ReferenceOnlyLoanReceipt(String memberId, String[] bookIds, String roomNumber) {
        super(memberId, bookIds);
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("roomNumber is required");
        }
        this.roomNumber = roomNumber.trim();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public ReferenceOnlyLoanReceipt withCorrectedBookId(int index, String newId) {
        return new ReferenceOnlyLoanReceipt(getMemberId(), replaced(index, newId), roomNumber);
    }
}

public class CirculationLedger {
    public static void main(String[] args) {
        try {
            new LoanReceipt("LIB-8841", new String[]{"BK-100", "bad"});
        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected (" + e.getMessage() + ")");
        }

        String[] input = {"BK-100", "BK-101"};
        LoanReceipt r = new LoanReceipt("LIB-8841", input);
        input[1] = "CHANGED";
        String[] ids = r.getBookIds();
        ids[0] = "HACKED";
        System.out.println(r.getBookIds()[0] + ", " + r.getBookIds()[1]);

        LoanReceipt fixed = r.withCorrectedBookId(1, "BK-109");
        System.out.println("original: " + String.join(", ", r.getBookIds()));
        System.out.println("corrected: " + String.join(", ", fixed.getBookIds()));

        LoanReceipt[] night = {
                new ReferenceOnlyLoanReceipt("LIB-001", new String[]{"BK-200"}, "Reading Room 3"),
                null,
                new LoanReceipt("LIB-002", new String[]{"BK-201"})
        };
        System.out.println(LoanReceipt.processNightlyCirculation(night));
    }
}
