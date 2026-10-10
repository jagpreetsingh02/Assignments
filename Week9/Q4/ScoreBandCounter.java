public class ScoreBandCounter {

    // Linear scan: O(n) time, O(1) space. Binary search: O(log n) time, O(1) space.
    // A plain binary search stops at any match. With duplicates at the edges (e.g. three 58s),
    // that could land in the middle of the run and miscount. So both searches keep moving
    // past matches until they reach a boundary: the first index >= low and the first index > high.
    static int countInBand(int[] scores, int low, int high) {
        return firstIndexAbove(scores, high) - firstIndexAtLeast(scores, low);
    }

    static int firstIndexAtLeast(int[] scores, int target) {
        int lo = 0;
        int hi = scores.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (scores[mid] >= target) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    static int firstIndexAbove(int[] scores, int target) {
        int lo = 0;
        int hi = scores.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (scores[mid] > target) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] scores = {35, 42, 42, 50, 58, 58, 58, 63, 71, 88};
        System.out.println(countInBand(scores, 42, 58));
        System.out.println(countInBand(scores, 90, 100));
        System.out.println(countInBand(scores, 0, 100));
    }
}
