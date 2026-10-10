public final class SurgeFeeCalculator {
    private final double minimumSurgePercent;

    public SurgeFeeCalculator(double minimumSurgePercent) {
        if (minimumSurgePercent < 0) {
            throw new IllegalArgumentException("Minimum surge percent cannot be negative");
        }
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

        double tieredPercent = tier1 * 0.5 + tier2 * 1.0 + tier3 * 2.0;
        double tieredFee = orderValue * tieredPercent / 100;
        double floorFee = orderValue * minimumSurgePercent / 100;

        return Math.max(tieredFee, floorFee);
    }

    public static void main(String[] args) {
        SurgeFeeCalculator calc = new SurgeFeeCalculator(1.0);

        int[] delays = {0, 1, 6, 15, 16};
        for (int d : delays) {
            System.out.println("orderValue = 500, delayMinutes = " + d + " -> Rs " + calc.calculateSurgeFee(500, d));
        }

        try {
            calc.calculateSurgeFee(-100, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
