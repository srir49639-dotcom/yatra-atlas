package services;

import algorithms.string.*;
import data.TravelData;
import model.*;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified Search Service powering customer exploration.
 * Integrates KMP, Z-Algorithm, Rabin-Karp, and Naive matching via AlgorithmSelector.
 * Customer UI sees only rich travel results; academic logs record internal execution metrics.
 */
public class SearchService {

    public static class SearchHit {
        public String destinationId;
        public String destinationName;
        public String state;
        public String itemType;      // "Destination", "Attraction", "Hotel", "Restaurant"
        public String title;
        public String subtitle;
        public String snippet;
        public String image;
        public String category;

        public SearchHit(String destinationId, String destinationName, String state,
                         String itemType, String title, String subtitle, String snippet,
                         String image, String category) {
            this.destinationId = destinationId;
            this.destinationName = destinationName;
            this.state = state;
            this.itemType = itemType;
            this.title = title;
            this.subtitle = subtitle;
            this.snippet = snippet;
            this.image = image;
            this.category = category;
        }
    }

    public static class SearchResponse {
        public String query;
        public String algorithmUsed;
        public double executionTimeMicros;
        public List<SearchHit> results;
        public String didYouMean; // Populated by fuzzy search if exact matches are low

        public SearchResponse(String query, String algorithmUsed, double executionTimeMicros,
                              List<SearchHit> results, String didYouMean) {
            this.query = query;
            this.algorithmUsed = algorithmUsed;
            this.executionTimeMicros = executionTimeMicros;
            this.results = results;
            this.didYouMean = didYouMean;
        }
    }

    /**
     * Performs comprehensive travel search across destinations, attractions, hotels, and restaurants.
     */
    public static SearchResponse search(String query) {
        if (query == null) query = "";
        query = query.trim();
        List<SearchHit> hits = new ArrayList<>();

        if (query.isEmpty()) {
            return new SearchResponse("", "None", 0, hits, null);
        }

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        // Sample text for algorithm selection strategy
        String sampleText = TravelData.DESTINATIONS[0].getDescription();
        AlgorithmSelector.SearchStrategy strategy = AlgorithmSelector.selectSearchAlgorithm(query, sampleText);

        for (Destination d : TravelData.DESTINATIONS) {
            // Check destination name, state, category, description
            boolean matchDest = matches(d.getName(), query, strategy) ||
                               matches(d.getState(), query, strategy) ||
                               matches(d.getCategory(), query, strategy) ||
                               matches(d.getDescription(), query, strategy);

            if (matchDest) {
                hits.add(new SearchHit(
                    d.getId(), d.getName(), d.getState(), "Destination",
                    d.getName(), d.getState() + " • " + d.getCategory(),
                    d.getDescription(), d.getImage(), d.getCategory()
                ));
            }

            // Check attractions
            for (Attraction a : d.getAttractions()) {
                if (matches(a.getName(), query, strategy) ||
                    matches(a.getCategory(), query, strategy) ||
                    matches(a.getDescription(), query, strategy)) {
                    hits.add(new SearchHit(
                        d.getId(), d.getName(), d.getState(), "Attraction",
                        a.getName(), a.getCategory() + " in " + d.getName(),
                        a.getDescription(), a.getImage(), a.getCategory()
                    ));
                }
            }

            // Check hotels
            for (Hotel h : d.getHotels()) {
                boolean matchAmenity = false;
                for (String am : h.getAmenities()) {
                    if (matches(am, query, strategy)) {
                        matchAmenity = true;
                        break;
                    }
                }
                if (matchAmenity || matches(h.getName(), query, strategy) || matches(h.getTier(), query, strategy)) {
                    hits.add(new SearchHit(
                        d.getId(), d.getName(), d.getState(), "Hotel",
                        h.getName(), h.getTier() + " • ₹" + h.getPricePerNight() + "/night",
                        "Rating " + h.getRating() + "★ • " + h.getAddress(),
                        d.getImage(), "Hospitality"
                    ));
                }
            }

            // Check restaurants
            for (Restaurant r : d.getRestaurants()) {
                if (matches(r.getName(), query, strategy) ||
                    matches(r.getCuisine(), query, strategy) ||
                    matches(r.getFamousDish(), query, strategy)) {
                    hits.add(new SearchHit(
                        d.getId(), d.getName(), d.getState(), "Restaurant",
                        r.getName(), r.getCuisine() + " • " + r.getFamousDish(),
                        "Avg ₹" + r.getAvgCostForTwo() + " for two • Rating " + r.getRating() + "★",
                        d.getImage(), "Dining"
                    ));
                }
            }
        }

        timer.stop();
        double timeMicros = timer.getElapsedMicros();

        // Record decision in CO1 log
        String algoName = strategy.name();
        String rationale = "Selected based on pattern length " + query.length() + " and token frequency";
        String complexity = (strategy == AlgorithmSelector.SearchStrategy.KMP) ? "O(N + M)" :
                            (strategy == AlgorithmSelector.SearchStrategy.Z_ALGORITHM) ? "O(N + M)" :
                            (strategy == AlgorithmSelector.SearchStrategy.RABIN_KARP) ? "O(N + M) avg" : "O(N * M)";
        AlgorithmSelector.recordDecision("STRING_SEARCH", algoName, rationale, complexity, timeMicros);

        // Check if typo correction is recommended
        String didYouMean = null;
        if (hits.size() <= 1) {
            didYouMean = FuzzySearchService.suggestCorrection(query);
            if (didYouMean != null && didYouMean.equalsIgnoreCase(query)) {
                didYouMean = null;
            }
        }

        return new SearchResponse(query, algoName, timeMicros, hits, didYouMean);
    }

    private static boolean matches(String text, String pattern, AlgorithmSelector.SearchStrategy strategy) {
        if (text == null || pattern == null) return false;
        switch (strategy) {
            case KMP:
                return KMP.contains(text, pattern);
            case Z_ALGORITHM:
                return ZAlgorithm.contains(text, pattern);
            case RABIN_KARP:
                return RabinKarp.contains(text, pattern);
            case NAIVE:
            default:
                return NaiveSearch.contains(text, pattern);
        }
    }
}
