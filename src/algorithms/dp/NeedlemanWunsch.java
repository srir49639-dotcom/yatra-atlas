package algorithms.dp;

/**
 * Needleman-Wunsch Global Sequence Alignment Algorithm.
 * Computes optimal global alignment between two sequences (e.g., token sequences or character strings).
 * Used in Yatra Atlas to compare destination profiles, attraction themes, and travel summaries globally.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(M * N), Space Complexity: O(M * N).
 */
public class NeedlemanWunsch {

    public static final int MATCH_SCORE = 2;
    public static final int MISMATCH_PENALTY = -1;
    public static final int GAP_PENALTY = -1;

    public static class AlignmentResult {
        public int score;
        public String alignedSeq1;
        public String alignedSeq2;
        public double normalizedSimilarity;

        public AlignmentResult(int score, String alignedSeq1, String alignedSeq2, double normalizedSimilarity) {
            this.score = score;
            this.alignedSeq1 = alignedSeq1;
            this.alignedSeq2 = alignedSeq2;
            this.normalizedSimilarity = normalizedSimilarity;
        }
    }

    /**
     * Performs global sequence alignment between seq1 and seq2.
     */
    public static AlignmentResult align(String seq1, String seq2) {
        if (seq1 == null) seq1 = "";
        if (seq2 == null) seq2 = "";

        int m = seq1.length();
        int n = seq2.length();

        int[][] dp = new int[m + 1][n + 1];

        // Base case: gaps along the borders
        for (int i = 0; i <= m; i++) dp[i][0] = i * GAP_PENALTY;
        for (int j = 0; j <= n; j++) dp[0][j] = j * GAP_PENALTY;

        // Fill DP matrix
        for (int i = 1; i <= m; i++) {
            char c1 = Character.toLowerCase(seq1.charAt(i - 1));
            for (int j = 1; j <= n; j++) {
                char c2 = Character.toLowerCase(seq2.charAt(j - 1));

                int matchCost = (c1 == c2) ? MATCH_SCORE : MISMATCH_PENALTY;
                int diagonal = dp[i - 1][j - 1] + matchCost;
                int up = dp[i - 1][j] + GAP_PENALTY;
                int left = dp[i][j - 1] + GAP_PENALTY;

                int maxVal = diagonal;
                if (up > maxVal) maxVal = up;
                if (left > maxVal) maxVal = left;

                dp[i][j] = maxVal;
            }
        }

        // Traceback to reconstruct aligned sequences
        char[] aligned1 = new char[m + n + 1];
        char[] aligned2 = new char[m + n + 1];
        int pos = 0;

        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                char c1 = Character.toLowerCase(seq1.charAt(i - 1));
                char c2 = Character.toLowerCase(seq2.charAt(j - 1));
                int matchCost = (c1 == c2) ? MATCH_SCORE : MISMATCH_PENALTY;

                if (dp[i][j] == dp[i - 1][j - 1] + matchCost) {
                    aligned1[pos] = seq1.charAt(i - 1);
                    aligned2[pos] = seq2.charAt(j - 1);
                    pos++;
                    i--;
                    j--;
                    continue;
                }
            }

            if (i > 0 && dp[i][j] == dp[i - 1][j] + GAP_PENALTY) {
                aligned1[pos] = seq1.charAt(i - 1);
                aligned2[pos] = '-';
                pos++;
                i--;
            } else if (j > 0) {
                aligned1[pos] = '-';
                aligned2[pos] = seq2.charAt(j - 1);
                pos++;
                j--;
            }
        }

        // Reverse reconstructed characters
        char[] rev1 = new char[pos];
        char[] rev2 = new char[pos];
        for (int k = 0; k < pos; k++) {
            rev1[k] = aligned1[pos - 1 - k];
            rev2[k] = aligned2[pos - 1 - k];
        }

        int score = dp[m][n];
        int maxPossibleScore = (m > n ? m : n) * MATCH_SCORE;
        double normSim = maxPossibleScore > 0 ? (double) Math.max(0, score) / maxPossibleScore : 1.0;

        return new AlignmentResult(score, new String(rev1), new String(rev2), normSim);
    }
}
