package algorithms.string;

/**
 * Z-Algorithm for linear time string matching.
 * Constructs the Z-array where Z[i] is the length of the longest common prefix
 * between S and suffix of S starting at index i.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N + M), Space Complexity: O(N + M) auxiliary.
 */
public class ZAlgorithm {

    /**
     * Computes the Z-array for a given string.
     * Uses the [L, R] interval window to achieve O(length) linear time.
     */
    public static int[] computeZArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        if (n == 0) return z;
        z[0] = n;

        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i > r) {
                l = r = i;
                while (r < n && Character.toLowerCase(s.charAt(r - l)) == Character.toLowerCase(s.charAt(r))) {
                    r++;
                }
                z[i] = r - l;
                r--;
            } else {
                int k = i - l;
                if (z[k] < r - i + 1) {
                    z[i] = z[k];
                } else {
                    l = i;
                    while (r < n && Character.toLowerCase(s.charAt(r - l)) == Character.toLowerCase(s.charAt(r))) {
                        r++;
                    }
                    z[i] = r - l;
                    r--;
                }
            }
        }
        return z;
    }

    /**
     * Searches for occurrences of pattern in text using Z-Algorithm.
     * Concatenates pattern + delimiter + text.
     */
    public static int[] search(String text, String pattern) {
        if (text == null || pattern == null || pattern.length() == 0 || text.length() < pattern.length()) {
            return new int[0];
        }

        int m = pattern.length();
        int n = text.length();

        // Delimiter that won't match standard alphanumeric search input
        String concat = pattern + "\u0001" + text;
        int[] z = computeZArray(concat);

        int[] tempMatches = new int[n];
        int count = 0;

        for (int i = m + 1; i < concat.length(); i++) {
            if (z[i] == m) {
                tempMatches[count++] = i - m - 1;
            }
        }

        int[] result = new int[count];
        for (int k = 0; k < count; k++) {
            result[k] = tempMatches[k];
        }
        return result;
    }

    public static boolean contains(String text, String pattern) {
        return search(text, pattern).length > 0;
    }
}
