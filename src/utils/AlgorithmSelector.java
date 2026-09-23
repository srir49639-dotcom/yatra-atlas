package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelligent Algorithm Selector & Strategy Engine (Course Outcome 1).
 * Dynamically determines the optimal algorithm based on problem type, input constraints,
 * and structural properties. Maintains an audit log for academic evaluation and viva verification.
 */
public class AlgorithmSelector {

    public static class SelectionRecord {
        public long timestamp;
        public String feature;
        public String selectedAlgorithm;
        public String rationale;
        public String complexity;
        public double timeTakenMicros;

        public SelectionRecord(String feature, String selectedAlgorithm, String rationale,
                               String complexity, double timeTakenMicros) {
            this.timestamp = System.currentTimeMillis();
            this.feature = feature;
            this.selectedAlgorithm = selectedAlgorithm;
            this.rationale = rationale;
            this.complexity = complexity;
            this.timeTakenMicros = timeTakenMicros;
        }
    }

    private static final List<SelectionRecord> decisionLogs = new ArrayList<>();
    private static final int MAX_LOGS = 50;

    public enum SearchStrategy {
        KMP,
        Z_ALGORITHM,
        RABIN_KARP,
        NAIVE
    }

    /**
     * Determines optimal string search algorithm based on pattern length and text structure.
     */
    public static SearchStrategy selectSearchAlgorithm(String pattern, String text) {
        if (pattern == null || text == null) return SearchStrategy.NAIVE;
        int m = pattern.length();

        if (m <= 3) {
            return SearchStrategy.NAIVE; // Minimal O(1) preprocessing overhead for short queries
        }

        // Check if pattern has repetitive prefix (e.g. "fort", "beach", "temple", "maha")
        if (hasRepeatingPrefix(pattern)) {
            return SearchStrategy.KMP; // KMP handles repeating prefix optimal skips
        }

        if (m > 12) {
            return SearchStrategy.RABIN_KARP; // Rolling hash is efficient for long phrase matching
        }

        return SearchStrategy.Z_ALGORITHM; // Z-Algorithm gives linear O(N+M) prefix window matching
    }

    private static boolean hasRepeatingPrefix(String p) {
        if (p.length() < 2) return false;
        char first = Character.toLowerCase(p.charAt(0));
        for (int i = 1; i < p.length(); i++) {
            if (Character.toLowerCase(p.charAt(i)) == first) return true;
        }
        return false;
    }

    /**
     * Records a decision in the audit log.
     */
    public static synchronized void recordDecision(String feature, String algorithm, String rationale,
                                                   String complexity, double timeTakenMicros) {
        if (decisionLogs.size() >= MAX_LOGS) {
            decisionLogs.remove(0);
        }
        decisionLogs.add(new SelectionRecord(feature, algorithm, rationale, complexity, timeTakenMicros));
    }

    /**
     * Retrieves recent algorithm selection decisions for academic documentation.
     */
    public static synchronized List<SelectionRecord> getRecentDecisions() {
        return new ArrayList<>(decisionLogs);
    }
}
