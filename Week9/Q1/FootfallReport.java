import java.util.ArrayList;
import java.util.List;

public class FootfallReport {

    // Naive: each query re-adds its range, so q queries cost O(n * q) in the worst case.
    // Optimized: one O(n) pass builds prefix sums, then each query is O(1), so O(n + q) total.
    // Extra space is O(n) for the prefix array. Building it pays off as soon as there are
    // more than a handful of queries on the same data, because every later query is free.
    static List<Long> footfallReport(int[] visitors, int[][] queries) {
        long[] prefix = new long[visitors.length + 1];
        for (int i = 0; i < visitors.length; i++) {
            prefix[i + 1] = prefix[i] + visitors[i];
        }

        List<Long> result = new ArrayList<>();
        for (int[] q : queries) {
            int start = q[0];
            int end = q[1];
            result.add(prefix[end + 1] - prefix[start]);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] visitors = {12, 7, 3, 9, 15, 4, 8};
        int[][] queries = {{0, 2}, {2, 5}, {4, 6}, {3, 3}};
        System.out.println(footfallReport(visitors, queries));
    }
}
