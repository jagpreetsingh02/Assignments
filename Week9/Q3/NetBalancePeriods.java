import java.util.HashMap;
import java.util.Map;

public class NetBalancePeriods {

    // Brute force sums every subarray: O(n^2) with a running sum, O(n^3) if each sum is rebuilt.
    // Prefix sums + hash map: one pass, O(n) time, O(n) extra space for the map.
    static int countPeriods(int[] transactions, long k) {
        Map<Long, Integer> seen = new HashMap<>();
        seen.put(0L, 1);

        long running = 0;
        int count = 0;

        for (int t : transactions) {
            running += t;
            count += seen.getOrDefault(running - k, 0);
            seen.put(running, seen.getOrDefault(running, 0) + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(countPeriods(new int[]{3, 4, -7, 1, 3, 3, 1, -4}, 7));
        System.out.println(countPeriods(new int[]{1, 2, 3}, 10));
    }
}
