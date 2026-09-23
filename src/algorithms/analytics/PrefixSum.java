package algorithms.analytics;

/**
 * Prefix Sum (Cumulative Scan) Algorithm.
 * Computes 1D running cumulative sum in linear O(N) time and answers range queries in O(1) time.
 * Real-world application: Cumulative itinerary metrics (distance traveled, running trip expenditure).
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N) preprocessing, O(1) query time.
 * Space Complexity: O(N) auxiliary.
 */
public class PrefixSum {

    private int[] prefix;

    /**
     * Initializes prefix sum array of size n + 1.
     * prefix[i] stores the sum of elements from values[0] to values[i-1].
     */
    public PrefixSum(int[] values) {
        if (values == null || values.length == 0) {
            this.prefix = new int[1];
            return;
        }

        int n = values.length;
        this.prefix = new int[n + 1];
        this.prefix[0] = 0;

        for (int i = 0; i < n; i++) {
            this.prefix[i + 1] = this.prefix[i] + values[i];
        }
    }

    /**
     * Returns cumulative sum from index 0 to index k (inclusive).
     */
    public int cumulativeAt(int k) {
        if (k < 0) return 0;
        if (k >= prefix.length - 1) return prefix[prefix.length - 1];
        return prefix[k + 1];
    }

    /**
     * Returns range sum in interval [left, right] inclusive in O(1) time.
     */
    public int rangeSum(int left, int right) {
        if (left < 0) left = 0;
        if (right >= prefix.length - 1) right = prefix.length - 2;
        if (left > right) return 0;
        return prefix[right + 1] - prefix[left];
    }

    /**
     * Returns the full raw prefix array.
     */
    public int[] getPrefixArray() {
        return prefix;
    }
}
