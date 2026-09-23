package algorithms.randomized;

/**
 * Reservoir Sampling Algorithm (Algorithm R).
 * Uniformly samples k items from a population of size N in a single pass O(N) time.
 * Real-world application: "Discover Something New / Surprise Me" destination discovery.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N), Space Complexity: O(k) auxiliary.
 */
public class ReservoirSampling {

    private static long lcgState = 0x123456789L;

    private static synchronized int nextRandomInt(int maxExclusive) {
        if (maxExclusive <= 0) return 0;
        lcgState = (lcgState * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        long val = Math.abs(lcgState) % maxExclusive;
        return (int) val;
    }

    /**
     * Samples k distinct indices from population of size n.
     * Every element has an exact equal probability (k / n) of being selected.
     */
    public static int[] sampleIndices(int n, int k) {
        if (n <= 0 || k <= 0) return new int[0];
        if (k > n) k = n;

        int[] reservoir = new int[k];

        // Fill the reservoir with the first k elements
        for (int i = 0; i < k; i++) {
            reservoir[i] = i;
        }

        // Iterate through remaining elements and replace with decreasing probability
        for (int i = k; i < n; i++) {
            int j = nextRandomInt(i + 1);
            if (j < k) {
                reservoir[j] = i;
            }
        }

        return reservoir;
    }

    /**
     * Generic helper for array of objects (Strings or IDs)
     */
    public static String[] sampleStrings(String[] stream, int k) {
        if (stream == null || stream.length == 0 || k <= 0) return new String[0];
        int n = stream.length;
        int[] indices = sampleIndices(n, k);
        String[] result = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            result[i] = stream[indices[i]];
        }
        return result;
    }
}
