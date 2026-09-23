package algorithms.dp;

/**
 * Damerau-Levenshtein Distance Algorithm (Optimal String Alignment).
 * Extends classic Levenshtein by counting adjacent character transpositions as 1 edit.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(M * N), Space Complexity: O(M * N) auxiliary.
 */
public class DamerauLevenshtein {

    /**
     * Computes the Damerau-Levenshtein distance between source and target.
     * Accurately penalizes or credits adjacent transpositions.
     */
    public static int distance(String source, String target) {
        if (source == null && target == null) return 0;
        if (source == null || source.length() == 0) return target == null ? 0 : target.length();
        if (target == null || target.length() == 0) return source.length();

        int sLen = source.length();
        int tLen = target.length();

        int[][] d = new int[sLen + 1][tLen + 1];

        for (int i = 0; i <= sLen; i++) d[i][0] = i;
        for (int j = 0; j <= tLen; j++) d[0][j] = j;

        for (int i = 1; i <= sLen; i++) {
            char sChar = Character.toLowerCase(source.charAt(i - 1));
            for (int j = 1; j <= tLen; j++) {
                char tChar = Character.toLowerCase(target.charAt(j - 1));

                int cost = (sChar == tChar) ? 0 : 1;

                int deletion = d[i - 1][j] + 1;
                int insertion = d[i][j - 1] + 1;
                int substitution = d[i - 1][j - 1] + cost;

                int minVal = deletion;
                if (insertion < minVal) minVal = insertion;
                if (substitution < minVal) minVal = substitution;

                d[i][j] = minVal;

                // Check for transposition
                if (i > 1 && j > 1) {
                    char prevS = Character.toLowerCase(source.charAt(i - 2));
                    char prevT = Character.toLowerCase(target.charAt(j - 2));

                    if (sChar == prevT && prevS == tChar) {
                        int transposition = d[i - 2][j - 2] + cost;
                        if (transposition < d[i][j]) {
                            d[i][j] = transposition;
                        }
                    }
                }
            }
        }
        return d[sLen][tLen];
    }

    /**
     * Normalized similarity score [0.0 to 1.0].
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
