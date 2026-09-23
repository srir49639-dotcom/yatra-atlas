package algorithms.string;

/**
 * Naive (Brute Force) Pattern Matching Algorithm.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N * M) worst case, Space Complexity: O(1) auxiliary.
 */
public class NaiveSearch {

    /**
     * Finds all 0-based starting indices of pattern in text.
     * @param text The source text
     * @param pattern The search query
     * @return int array of match start indices
     */
    public static int[] search(String text, String pattern) {
        if (text == null || pattern == null || pattern.length() == 0 || text.length() < pattern.length()) {
            return new int[0];
        }

        int n = text.length();
        int m = pattern.length();
        int[] tempMatches = new int[n - m + 1];
        int count = 0;

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (Character.toLowerCase(text.charAt(i + j)) != Character.toLowerCase(pattern.charAt(j))) {
                    break;
                }
            }
            if (j == m) {
                tempMatches[count++] = i;
            }
        }

        int[] result = new int[count];
        for (int k = 0; k < count; k++) {
            result[k] = tempMatches[k];
        }
        return result;
    }

    /**
     * Checks if pattern exists anywhere in text.
     */
    public static boolean contains(String text, String pattern) {
        return search(text, pattern).length > 0;
    }
}
