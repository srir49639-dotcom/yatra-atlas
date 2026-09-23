package services;

import algorithms.dp.DamerauLevenshtein;
import algorithms.dp.Levenshtein;
import data.TravelData;
import model.Attraction;
import model.Destination;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Fuzzy Search & Spell Correction Service.
 * Leverages Damerau-Levenshtein and Levenshtein algorithms to correct user typos.
 * Handles insertions, deletions, substitutions, and adjacent character transpositions.
 */
public class FuzzySearchService {

    public static class FuzzyMatch {
        public String term;
        public String type;        // Destination, Attraction, City
        public int distance;
        public double similarity;
        public String algorithm;

        public FuzzyMatch(String term, String type, int distance, double similarity, String algorithm) {
            this.term = term;
            this.type = type;
            this.distance = distance;
            this.similarity = similarity;
            this.algorithm = algorithm;
        }
    }

    /**
     * Finds the closest match for a possibly misspelled query.
     */
    public static String suggestCorrection(String query) {
        if (query == null || query.trim().length() < 3) return null;
        query = query.trim();

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        List<String> dictionary = buildDictionary();
        String bestCandidate = null;
        int minDistance = Integer.MAX_VALUE;
        String algorithmUsed = "Damerau-Levenshtein";

        // Standard Indian City Aliases & Common Transliterations
        java.util.Map<String, String> aliasMap = getIndianCityAliases();
        String lowerQuery = query.toLowerCase();
        if (aliasMap.containsKey(lowerQuery)) {
            String canonical = aliasMap.get(lowerQuery);
            int dist = DamerauLevenshtein.distance(lowerQuery, canonical.toLowerCase());
            AlgorithmSelector.recordDecision(
                "FUZZY_SPELL_CHECK",
                "Alias Mapping + Damerau-Levenshtein",
                "Query alias '" + query + "' resolved to canonical '" + canonical + "'",
                "O(M * N)",
                timer.getElapsedMicros()
            );
            return canonical;
        }

        for (String word : dictionary) {
            int dlDist = DamerauLevenshtein.distance(query, word);
            int lDist = Levenshtein.distance(query, word);

            int dist = dlDist;
            if (dlDist < lDist) {
                algorithmUsed = "Damerau-Levenshtein (Transposition Handled)";
            }

            if (dist < minDistance) {
                minDistance = dist;
                bestCandidate = word;
            }
        }

        // Also check distance against alias keys (e.g. "Banglore" -> "Bangalore" -> "Bengaluru")
        for (java.util.Map.Entry<String, String> entry : aliasMap.entrySet()) {
            int dlDist = DamerauLevenshtein.distance(lowerQuery, entry.getKey());
            if (dlDist < minDistance) {
                minDistance = dlDist;
                bestCandidate = entry.getValue();
                algorithmUsed = "Damerau-Levenshtein (Transliteration Match)";
            }
        }

        timer.stop();

        // If distance is reasonable (<= 3 edits or <= 40% of length)
        if (bestCandidate != null && minDistance > 0 && minDistance <= Math.max(3, query.length() / 2)) {
            AlgorithmSelector.recordDecision(
                "FUZZY_SPELL_CHECK",
                algorithmUsed,
                "Query '" + query + "' matched to '" + bestCandidate + "' with edit distance " + minDistance,
                "O(M * N)",
                timer.getElapsedMicros()
            );
            return bestCandidate;
        }

        return null;
    }

    private static java.util.Map<String, String> getIndianCityAliases() {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        map.put("banglore", "Bengaluru");
        map.put("bangalore", "Bengaluru");
        map.put("bombay", "Mumbai");
        map.put("calcutta", "Kolkata");
        map.put("madras", "Chennai");
        map.put("chenai", "Chennai");
        map.put("hydrabad", "Hyderabad");
        map.put("charminr", "Charminar");
        map.put("benares", "Varanasi");
        map.put("benaras", "Varanasi");
        map.put("kashi", "Varanasi");
        map.put("pondy", "Pondicherry");
        map.put("ootacamund", "Ooty");
        map.put("cochin", "Kochi");
        map.put("vizag", "Visakhapatnam");
        map.put("trivandrum", "Thiruvananthapuram");
        map.put("baroda", "Vadodara");
        map.put("mysore", "Mysuru");
        return map;
    }

    /**
     * Returns top ranked fuzzy candidates with similarity scores for technical inspection.
     */
    public static List<FuzzyMatch> getFuzzyMatches(String query, int maxResults) {
        List<FuzzyMatch> list = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) return list;
        query = query.trim();

        List<String> dict = buildDictionary();
        for (String word : dict) {
            int dlDist = DamerauLevenshtein.distance(query, word);
            double sim = DamerauLevenshtein.similarity(query, word);

            if (sim >= 0.55 || dlDist <= 3) {
                list.add(new FuzzyMatch(word, "Travel Term", dlDist, sim, "Damerau-Levenshtein"));
            }
        }

        // Check aliases
        for (java.util.Map.Entry<String, String> entry : getIndianCityAliases().entrySet()) {
            int dlDist = DamerauLevenshtein.distance(query.toLowerCase(), entry.getKey());
            if (dlDist <= 2) {
                double sim = DamerauLevenshtein.similarity(query.toLowerCase(), entry.getKey());
                list.add(new FuzzyMatch(entry.getValue(), "City Alias", dlDist, sim, "Damerau-Levenshtein"));
            }
        }

        // Sort by similarity descending
        list.sort((a, b) -> Double.compare(b.similarity, a.similarity));

        if (list.size() > maxResults) {
            return list.subList(0, maxResults);
        }
        return list;
    }

    private static List<String> buildDictionary() {
        List<String> dict = new ArrayList<>();
        for (Destination d : TravelData.DESTINATIONS) {
            dict.add(d.getName());
            dict.add(d.getState());
            dict.add(d.getCategory());
            for (Attraction a : d.getAttractions()) {
                dict.add(a.getName());
            }
        }
        return dict;
    }
}
