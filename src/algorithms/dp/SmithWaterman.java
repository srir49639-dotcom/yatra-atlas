package algorithms.dp;

/**
 * Smith-Waterman Local Sequence Alignment Algorithm.
 * Identifies the optimal locally similar sub-region between two sequences.
 * Used in Yatra Atlas to identify shared local travel themes, common attraction patterns, and matching descriptors.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(M * N), Space Complexity: O(M * N).
 */
public class SmithWaterman {

    public static final int MATCH_SCORE = 3;
    public static final int MISMATCH_PENALTY = -2;
    public static final int GAP_PENALTY = -2;

    public static class LocalAlignmentResult {
        public int maxScore;
        public String subSeq1;
        public String subSeq2;
        public int startPos1;
        public int endPos1;
        public int startPos2;
        public int endPos2;

        public LocalAlignmentResult(int maxScore, String subSeq1, String subSeq2,
                                    int startPos1, int endPos1, int startPos2, int endPos2) {
            this.maxScore = maxScore;
            this.subSeq1 = subSeq1;
            this.subSeq2 = subSeq2;
            this.startPos1 = startPos1;
            this.endPos1 = endPos1;
            this.startPos2 = startPos2;
            this.endPos2 = endPos2;
        }
    }

    /**
     * Finds the optimal local alignment between seq1 and seq2.
     */
    public static LocalAlignmentResult align(String seq1, String seq2) {
        if (seq1 == null) seq1 = "";
        if (seq2 == null) seq2 = "";

        int m = seq1.length();
        int n = seq2.length();

        int[][] dp = new int[m + 1][n + 1];
        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        // Fill matrix; border values are 0 by default in Java primitive arrays
        for (int i = 1; i <= m; i++) {
            char c1 = Character.toLowerCase(seq1.charAt(i - 1));
            for (int j = 1; j <= n; j++) {
                char c2 = Character.toLowerCase(seq2.charAt(j - 1));

                int matchCost = (c1 == c2) ? MATCH_SCORE : MISMATCH_PENALTY;
                int diagonal = dp[i - 1][j - 1] + matchCost;
                int up = dp[i - 1][j] + GAP_PENALTY;
                int left = dp[i][j - 1] + GAP_PENALTY;

                int cellMax = 0; // Smith-Waterman lower bound
                if (diagonal > cellMax) cellMax = diagonal;
                if (up > cellMax) cellMax = up;
                if (left > cellMax) cellMax = left;

                dp[i][j] = cellMax;

                if (cellMax > maxScore) {
                    maxScore = cellMax;
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        // Traceback from maximum scoring cell until cell value drops to 0
        char[] aligned1 = new char[m + n + 1];
        char[] aligned2 = new char[m + n + 1];
        int pos = 0;

        int i = maxI;
        int j = maxJ;
        int end1 = maxI, end2 = maxJ;

        while (i > 0 && j > 0 && dp[i][j] > 0) {
            char c1 = Character.toLowerCase(seq1.charAt(i - 1));
            char c2 = Character.toLowerCase(seq2.charAt(j - 1));
            int matchCost = (c1 == c2) ? MATCH_SCORE : MISMATCH_PENALTY;

            if (dp[i][j] == dp[i - 1][j - 1] + matchCost) {
                aligned1[pos] = seq1.charAt(i - 1);
                aligned2[pos] = seq2.charAt(j - 1);
                pos++;
                i--;
                j--;
            } else if (dp[i][j] == dp[i - 1][j] + GAP_PENALTY) {
                aligned1[pos] = seq1.charAt(i - 1);
                aligned2[pos] = '-';
                pos++;
                i--;
            } else {
                aligned1[pos] = '-';
                aligned2[pos] = seq2.charAt(j - 1);
                pos++;
                j--;
            }
        }

        char[] rev1 = new char[pos];
        char[] rev2 = new char[pos];
        for (int k = 0; k < pos; k++) {
            rev1[k] = aligned1[pos - 1 - k];
            rev2[k] = aligned2[pos - 1 - k];
        }

        return new LocalAlignmentResult(maxScore, new String(rev1), new String(rev2), i, end1, j, end2);
    }
}
