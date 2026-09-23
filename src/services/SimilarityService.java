package services;

import algorithms.dp.NeedlemanWunsch;
import algorithms.dp.SmithWaterman;
import data.TravelData;
import model.Destination;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Destination Similarity & Comparative Alignment Service.
 * Implements:
 * 1. Multi-feature document similarity for "Similar Destinations" ("You may also like").
 * 2. Needleman-Wunsch global sequence alignment for comprehensive comparative thematic matching.
 * 3. Smith-Waterman local sequence alignment for extracting shared local travel themes.
 */
public class SimilarityService {

    public static class SimilarDestinationItem {
        public Destination destination;
        public double score;
        public String matchReason;

        public SimilarDestinationItem(Destination destination, double score, String matchReason) {
            this.destination = destination;
            this.score = score;
            this.matchReason = matchReason;
        }
    }

    public static class ComparisonReport {
        public Destination destA;
        public Destination destB;
        public double overallSimilarity;
        public String sharedThemes;
        public NeedlemanWunsch.AlignmentResult globalAlignment;
        public SmithWaterman.LocalAlignmentResult localAlignment;
        public double timeTakenMicros;

        public ComparisonReport(Destination destA, Destination destB, double overallSimilarity,
                                String sharedThemes, NeedlemanWunsch.AlignmentResult globalAlignment,
                                SmithWaterman.LocalAlignmentResult localAlignment, double timeTakenMicros) {
            this.destA = destA;
            this.destB = destB;
            this.overallSimilarity = overallSimilarity;
            this.sharedThemes = sharedThemes;
            this.globalAlignment = globalAlignment;
            this.localAlignment = localAlignment;
            this.timeTakenMicros = timeTakenMicros;
        }
    }

    /**
     * Calculates calculated similar destinations for "You may also like".
     * Dynamically compares categories, budget proximity, and description vocabulary.
     */
    public static List<SimilarDestinationItem> findSimilarDestinations(String destinationId, int limit) {
        List<SimilarDestinationItem> list = new ArrayList<>();
        Destination target = TravelData.getDestinationById(destinationId);
        if (target == null) return list;

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        Set<String> targetTokens = tokenize(target.getDescription() + " " + target.getCategory());

        for (Destination candidate : TravelData.DESTINATIONS) {
            if (candidate.getId().equalsIgnoreCase(target.getId())) continue;

            double score = 0.0;
            StringBuilder reasons = new StringBuilder();

            // Category match bonus
            if (candidate.getCategory().equalsIgnoreCase(target.getCategory())) {
                score += 0.45;
                reasons.append("Shared ").append(target.getCategory()).append(" experience. ");
            }

            // Budget proximity (within 30%)
            int budgetDiff = Math.abs(candidate.getAvgDailyBudget() - target.getAvgDailyBudget());
            double budgetSimilarity = Math.max(0, 1.0 - ((double) budgetDiff / Math.max(candidate.getAvgDailyBudget(), target.getAvgDailyBudget())));
            score += 0.25 * budgetSimilarity;

            // Jaccard similarity of vocabulary tokens
            Set<String> candidateTokens = tokenize(candidate.getDescription() + " " + candidate.getCategory());
            double jaccard = computeJaccard(targetTokens, candidateTokens);
            score += 0.30 * jaccard;

            if (score > 0.25) {
                if (reasons.length() == 0) reasons.append("Similar travel vibe and pricing.");
                list.add(new SimilarDestinationItem(candidate, score, reasons.toString().trim()));
            }
        }

        list.sort((a, b) -> Double.compare(b.score, a.score));
        timer.stop();

        AlgorithmSelector.recordDecision(
            "DOCUMENT_SIMILARITY",
            "Jaccard Vector Similarity + Domain Feature Weighting",
            "Evaluated " + (TravelData.DESTINATIONS.length - 1) + " candidate destinations against " + target.getName(),
            "O(N * |V|)",
            timer.getElapsedMicros()
        );

        if (list.size() > limit) {
            return list.subList(0, limit);
        }
        return list;
    }

    /**
     * Compares two destinations in depth using Needleman-Wunsch (Global) and Smith-Waterman (Local).
     */
    public static ComparisonReport compareDestinations(String idA, String idB) {
        Destination a = TravelData.getDestinationById(idA);
        Destination b = TravelData.getDestinationById(idB);
        if (a == null || b == null) return null;

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        // Needleman-Wunsch Global Alignment on descriptions
        NeedlemanWunsch.AlignmentResult global = NeedlemanWunsch.align(a.getDescription(), b.getDescription());

        // Smith-Waterman Local Alignment
        SmithWaterman.LocalAlignmentResult local = SmithWaterman.align(a.getDescription(), b.getDescription());

        timer.stop();

        String sharedTheme = "Both destinations offer rich cultural explorations and regional immersion.";
        if (a.getCategory().equalsIgnoreCase(b.getCategory())) {
            sharedTheme = "Both are prominent " + a.getCategory() + " hubs in India with complementary regional attractions.";
        }

        AlgorithmSelector.recordDecision(
            "SEQUENCE_ALIGNMENT",
            "Needleman-Wunsch & Smith-Waterman",
            "Aligned descriptions between " + a.getName() + " and " + b.getName() + ". Global score: " + global.score + ", Local score: " + local.maxScore,
            "O(M * N)",
            timer.getElapsedMicros()
        );

        return new ComparisonReport(a, b, global.normalizedSimilarity, sharedTheme, global, local, timer.getElapsedMicros());
    }

    private static Set<String> tokenize(String text) {
        Set<String> set = new HashSet<>();
        if (text == null) return set;
        String[] words = text.toLowerCase().split("[^a-z0-9]+");
        for (String w : words) {
            if (w.length() > 3) set.add(w);
        }
        return set;
    }

    private static double computeJaccard(Set<String> s1, Set<String> s2) {
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;
        int intersection = 0;
        for (String w : s1) {
            if (s2.contains(w)) intersection++;
        }
        int union = s1.size() + s2.size() - intersection;
        return union > 0 ? (double) intersection / union : 0.0;
    }
}
