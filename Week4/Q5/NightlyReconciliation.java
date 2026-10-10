final class SurgeFeeCalculator {
    private final double minimumSurgePercent;

    SurgeFeeCalculator(double minimumSurgePercent) {
        this.minimumSurgePercent = minimumSurgePercent;
    }

    final double calculateSurgeFee(double orderValue, int delayMinutes) {
        if (orderValue < 0 || delayMinutes < 0) {
            throw new IllegalArgumentException("Order value and delay must be non-negative");
        }
        if (delayMinutes == 0) {
            return 0.0;
        }

        int tier1 = Math.min(delayMinutes, 5);
        int tier2 = Math.max(0, Math.min(delayMinutes, 15) - 5);
        int tier3 = Math.max(0, delayMinutes - 15);

        double tieredFee = orderValue * (tier1 * 0.5 + tier2 * 1.0 + tier3 * 2.0) / 100;
        double floorFee = orderValue * minimumSurgePercent / 100;
        return Math.max(tieredFee, floorFee);
    }
}

class DeliveryAccount {
    private static final SurgeFeeCalculator CALCULATOR;
    private static final double PROVISIONAL_ORDER_VALUE;

    static {
        CALCULATOR = new SurgeFeeCalculator(1.0);
        PROVISIONAL_ORDER_VALUE = 0.0;
    }

    private final String studentId;
    private double orderValue;

    public DeliveryAccount(String studentId, double orderValue) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID is required");
        }
        this.studentId = studentId.trim();
        this.orderValue = orderValue;
    }

    public DeliveryAccount(String studentId) {
        this(studentId, PROVISIONAL_ORDER_VALUE);
    }

    // Reuses the tiered calculator from Problem 4.
    final double calculateSurgeFee(int delayMinutes) {
        return CALCULATOR.calculateSurgeFee(orderValue, delayMinutes);
    }

    void setOrderValue(double orderValue) {
        if (orderValue < 0) {
            throw new IllegalArgumentException("Order value cannot be negative");
        }
        this.orderValue = orderValue;
    }

    String getStudentId() {
        return studentId;
    }
}

class PremiumAccount extends DeliveryAccount {
    static final double SURGE_WAIVER = 0.5;

    public PremiumAccount(String studentId, double orderValue) {
        super(studentId, orderValue);
    }
}

public class NightlyReconciliation {
    private int processed;
    private int nullSkipped;
    private int premiumCount;
    private int regularCount;
    private double grandTotal;

    void processAccount(DeliveryAccount account, double amount, int delayMinutes) {
        if (account == null) {
            nullSkipped++;
            return;
        }

        account.setOrderValue(amount);
        double fee = account.calculateSurgeFee(delayMinutes);

        if (account instanceof PremiumAccount) {
            fee = fee * (1 - PremiumAccount.SURGE_WAIVER);
            premiumCount++;
        } else {
            regularCount++;
        }

        processed++;
        grandTotal += fee;
        System.out.printf("%s settled: Rs %.2f%n", account.getStudentId(), fee);
    }

    // If the three arrays disagree in length, nothing is processed. Charging one
    // student's amount to another student's account is far worse than a run that
    // stops before touching anything, so the whole batch is rejected up front.
    static void processBatch(DeliveryAccount[] accounts, double[] amounts, int[] delayMinutesArray) {
        if (accounts == null || amounts == null || delayMinutesArray == null) {
            System.out.println("Batch rejected: one of the input arrays is missing");
            return;
        }
        if (accounts.length != amounts.length || amounts.length != delayMinutesArray.length) {
            System.out.println("Batch rejected: array lengths do not match ("
                    + accounts.length + ", " + amounts.length + ", " + delayMinutesArray.length + ")");
            return;
        }

        NightlyReconciliation run = new NightlyReconciliation();
        for (int i = 0; i < accounts.length; i++) {
            try {
                run.processAccount(accounts[i], amounts[i], delayMinutesArray[i]);
            } catch (IllegalArgumentException e) {
                System.out.println("Index " + i + " skipped: " + e.getMessage());
            }
        }

        System.out.printf("%d processed | %d null skipped | %d premium | %d regular | grand total surge fees = Rs %.2f%n",
                run.processed, run.nullSkipped, run.premiumCount, run.regularCount, run.grandTotal);
    }

    public static void main(String[] args) {
        DeliveryAccount[] accounts = {
                new PremiumAccount("STU001", 500),
                null,
                new DeliveryAccount("STU002", 300)
        };
        processBatch(accounts, new double[]{500, 400, 300}, new int[]{10, 5, 0});

        System.out.println();
        processBatch(accounts, new double[]{500, 400}, new int[]{10, 5, 0});
    }
}
