package algorithms.string;

/**
 * Rabin-Karp Algorithm for string matching using rolling polynomial hashes.
 * Strict DSA-3: Pure array and arithmetic operations, NO java.util.* used.
 * Time Complexity: O(N + M) average, O(N * M) worst case.
 * Space Complexity: O(1) auxiliary.
 */
public class RabinKarp {

    private static final int BASE = 256;
    private static final long MODULUS = 1000000007L;

    /**
     * Searches for pattern in text using rolling hash.
     */
    public static int[] search(String text, String pattern) {
        if (text == null || pattern == null || pattern.length() == 0 || text.length() < pattern.length()) {
            return new int[0];
        }

        int n = text.length();
        int m = pattern.length();

        // Calculate highest power h = (BASE^(m-1)) % MODULUS
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % MODULUS;
        }

        long patternHash = 0;
        long textHash = 0;

        // Compute initial hash values for pattern and first window of text
        for (int i = 0; i < m; i++) {
            patternHash = (BASE * patternHash + Character.toLowerCase(pattern.charAt(i))) % MODULUS;
            textHash = (BASE * textHash + Character.toLowerCase(text.charAt(i))) % MODULUS;
        }

        int[] tempMatches = new int[n - m + 1];
        int count = 0;

        for (int i = 0; i <= n - m; i++) {
            // Check hash values
            if (patternHash == textHash) {
                // Character by character check on collision
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (Character.toLowerCase(text.charAt(i + j)) != Character.toLowerCase(pattern.charAt(j))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    tempMatches[count++] = i;
                }
            }

            // Calculate rolling hash for next window
            if (i < n - m) {
                textHash = (BASE * (textHash - Character.toLowerCase(text.charAt(i)) * h) + Character.toLowerCase(text.charAt(i + m))) % MODULUS;
                // If textHash is negative, convert to positive
                if (textHash < 0) {
                    textHash = (textHash + MODULUS);
                }
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
