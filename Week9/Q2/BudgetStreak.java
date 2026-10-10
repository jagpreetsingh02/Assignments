public class BudgetStreak {

    // Checking every run is O(n^2) (O(n^3) if each sum is recomputed).
    // Sliding window: each index enters and leaves the window at most once, so O(n) time, O(1) space.
    // This only works because costs are non-negative: adding a day can never lower the sum,
    // and dropping a day can never raise it. Once the window is over budget, shrinking from the
    // left is the only way back under. With negative values that guarantee breaks.
    static int[] longestStreak(int[] costs, long budget) {
        int bestLength = 0;
        int bestStart = -1;
        long sum = 0;
        int left = 0;

        for (int right = 0; right < costs.length; right++) {
            sum += costs[right];
            while (sum > budget) {
                sum -= costs[left];
                left++;
            }

            int length = right - left + 1;
            if (length > bestLength) {
                bestLength = length;
                bestStart = left;
            }
        }
        return new int[]{bestLength, bestStart};
    }

    static void print(int[] result) {
        System.out.println("(" + result[0] + ", " + result[1] + ")");
    }

    public static void main(String[] args) {
        print(longestStreak(new int[]{4, 2, 1, 7, 3, 1, 2, 1, 5}, 8));
        print(longestStreak(new int[]{9, 10}, 8));
    }
}
