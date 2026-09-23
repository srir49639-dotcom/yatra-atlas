package services;

import algorithms.string.LCP;
import algorithms.string.SuffixArray;
import data.TravelData;
import model.Destination;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

/**
 * Text Insights Service powered by Suffix Array and Kasai's LCP Algorithm.
 * Dissects destination descriptions to discover recurring phrases, cultural motifs,
 * and prominent travel keywords.
 */
public class TextAnalysisService {

    public static class TextInsights {
        public String destinationId;
        public String destinationName;
        public String[] recurringPhrases;
        public String longestRepeatedPhrase;
        public int characterCount;
        public int suffixArraySize;
        public double analysisTimeMicros;

        public TextInsights(String destinationId, String destinationName, String[] recurringPhrases,
                            String longestRepeatedPhrase, int characterCount, int suffixArraySize,
                            double analysisTimeMicros) {
            this.destinationId = destinationId;
            this.destinationName = destinationName;
            this.recurringPhrases = recurringPhrases;
            this.longestRepeatedPhrase = longestRepeatedPhrase;
            this.characterCount = characterCount;
            this.suffixArraySize = suffixArraySize;
            this.analysisTimeMicros = analysisTimeMicros;
        }
    }

    /**
     * Extracts text insights for a specific destination.
     */
    public static TextInsights analyzeDestination(String destinationId) {
        Destination d = TravelData.getDestinationById(destinationId);
        if (d == null) return null;

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        // Concatenate description and attraction descriptions for deep textual analysis
        StringBuilder fullText = new StringBuilder();
        fullText.append(d.getDescription()).append(" ");
        for (var a : d.getAttractions()) {
            fullText.append(a.getDescription()).append(" ");
        }

        String corpus = fullText.toString();
        int[] sa = SuffixArray.buildSuffixArray(corpus);
        int[] lcp = LCP.buildLCPArray(corpus, sa);

        String longestRepeated = LCP.findLongestRepeatedSubstring(corpus, sa, lcp);
        String[] recurring = LCP.extractRecurringPhrases(corpus, 4, 6);

        timer.stop();
        double timeTaken = timer.getElapsedMicros();

        AlgorithmSelector.recordDecision(
            "TEXT_INSIGHTS",
            "Suffix Array (Prefix Doubling) + Kasai LCP",
            "Analyzed " + corpus.length() + " chars of text for " + d.getName() + " to uncover repetitive motifs.",
            "O(N log^2 N + N)",
            timeTaken
        );

        return new TextInsights(d.getId(), d.getName(), recurring, longestRepeated, corpus.length(), sa.length, timeTaken);
    }
}
