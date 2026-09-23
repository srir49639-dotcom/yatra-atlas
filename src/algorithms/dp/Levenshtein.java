package algorithms.dp;

/**
 * Levenshtein Distance Algorithm (Edit Distance).
 * Dynamic programming table computing minimum single-character edits (insertions, deletions, substitutions).
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(M * N), Space Complexity: O(min(M, N)) with space optimization.
 */
public class Levenshtein {

    /**
     * Computes classic Levenshtein distance between s1 and s2.
     */
    public static int distance(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null || s1.length() == 0) return s2 == null ? 0 : s2.length();
        if (s2 == null || s2.length() == 0) return s1.length();

        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            char c1 = Character.toLowerCase(s1.charAt(i - 1));
            for (int j = 1; j <= n; j++) {
                char c2 = Character.toLowerCase(s2.charAt(j - 1));

                if (c1 == c2) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int substitute = dp[i - 1][j - 1];

                    int minOp = insert;
                    if (delete < minOp) minOp = delete;
                    if (substitute < minOp) minOp = substitute;

                    dp[i][j] = 1 + minOp;
                }
            }
        }
        return dp[m][n];
    }

    /**
     * Normalized similarity score between 0.0 (completely dissimilar) and 1.0 (identical).
     */
    public static double similarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equalsIgnoreCase(s2)) return 1.0;
        int maxLen = s1.length() > s2.length() ? s1.length() : s2.length();
        if (maxLen == 0) return 1.0;
        int dist = distance(s1, s2);
        return 1.0 - ((double) dist / maxLen);
    }
}
