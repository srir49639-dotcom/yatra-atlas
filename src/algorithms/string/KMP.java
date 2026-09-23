package algorithms.string;

/**
 * Knuth-Morris-Pratt (KMP) String Matching Algorithm.
 * Computes the Longest Proper Prefix which is also Suffix (LPS / Pi) array.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N + M), Space Complexity: O(M) auxiliary.
 */
public class KMP {

    /**
     * Computes the LPS (Longest Prefix Suffix) table for the pattern.
     * @param pattern Query string
     * @return int array where lps[i] is the length of the longest proper prefix of pattern[0..i]
     *         that is also a suffix of pattern[0..i].
     */
    public static int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < m) {
            char ci = Character.toLowerCase(pattern.charAt(i));
            char clen = Character.toLowerCase(pattern.charAt(len));

            if (ci == clen) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * Searches for all occurrences of pattern in text using KMP.
     * @param text Source text
     * @param pattern Pattern to search
     * @return int array containing starting indices of matches.
     */
    public static int[] search(String text, String pattern) {
        if (text == null || pattern == null || pattern.length() == 0 || text.length() < pattern.length()) {
            return new int[0];
        }

        int n = text.length();
        int m = pattern.length();
        int[] lps = computeLPS(pattern);

        int[] tempMatches = new int[n];
        int count = 0;

        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            char tc = Character.toLowerCase(text.charAt(i));
            char pc = Character.toLowerCase(pattern.charAt(j));

            if (tc == pc) {
                i++;
                j++;
            }

            if (j == m) {
                tempMatches[count++] = i - j;
                j = lps[j - 1];
            } else if (i < n && Character.toLowerCase(text.charAt(i)) != Character.toLowerCase(pattern.charAt(j))) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        int[] result = new int[count];
        for (int k = 0; k < count; k++) {
            result[k] = tempMatches[k];
        }
        return result;
    }

    /**
     * Checks if pattern exists in text.
     */
    public static boolean contains(String text, String pattern) {
        return search(text, pattern).length > 0;
    }
}
