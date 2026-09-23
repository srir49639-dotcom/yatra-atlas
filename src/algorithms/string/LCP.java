package algorithms.string;

/**
 * Longest Common Prefix (LCP) Array using Kasai's Algorithm.
 * Given text and its Suffix Array, computes LCP values in linear O(N) time.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N), Space Complexity: O(N) auxiliary.
 */
public class LCP {

    /**
     * Kasai's algorithm to build LCP array from text and its suffix array.
     * @param text Source text
     * @param sa Suffix array of text
     * @return int array where lcp[i] is the length of LCP between suffixes sa[i] and sa[i-1].
     *         lcp[0] is initialized to 0.
     */
    public static int[] buildLCPArray(String text, int[] sa) {
        if (text == null || sa == null || sa.length == 0) return new int[0];
        int n = text.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int h = 0; // current LCP length
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1]; // previous suffix in sorted order
                while (i + h < n && j + h < n &&
                       Character.toLowerCase(text.charAt(i + h)) == Character.toLowerCase(text.charAt(j + h))) {
                    h++;
                }
                lcp[rank[i]] = h;
                if (h > 0) {
                    h--;
                }
            } else {
                lcp[rank[i]] = 0;
            }
        }
        return lcp;
    }

    /**
     * Finds the longest repeated substring in the text using SA and LCP.
     */
    public static String findLongestRepeatedSubstring(String text, int[] sa, int[] lcp) {
        if (text == null || sa == null || lcp == null || lcp.length == 0) return "";
        int maxLen = 0;
        int maxIndex = -1;

        for (int i = 1; i < lcp.length; i++) {
            if (lcp[i] > maxLen) {
                maxLen = lcp[i];
                maxIndex = sa[i];
            }
        }

        if (maxLen > 0 && maxIndex != -1) {
            return text.substring(maxIndex, maxIndex + maxLen);
        }
        return "";
    }

    /**
     * Extracts repeated substrings of minimum length minLen from text.
     * Useful for extracting recurring phrases and themes in destination descriptions.
     */
    public static String[] extractRecurringPhrases(String text, int minLen, int maxResults) {
        if (text == null || text.length() < minLen) return new String[0];
        int[] sa = SuffixArray.buildSuffixArray(text);
        int[] lcp = buildLCPArray(text, sa);

        String[] temp = new String[lcp.length];
        int count = 0;

        for (int i = 1; i < lcp.length; i++) {
            if (lcp[i] >= minLen) {
                String candidate = text.substring(sa[i], sa[i] + lcp[i]).trim();
                // Check if candidate has meaningful letters and is not duplicate
                if (candidate.length() >= minLen && !contains(temp, count, candidate)) {
                    temp[count++] = candidate;
                    if (count >= maxResults) break;
                }
            }
        }

        String[] result = new String[count];
        for (int k = 0; k < count; k++) {
            result[k] = temp[k];
        }
        return result;
    }

    private static boolean contains(String[] arr, int size, String item) {
        for (int i = 0; i < size; i++) {
            if (arr[i] != null && arr[i].equalsIgnoreCase(item)) return true;
        }
        return false;
    }
}
