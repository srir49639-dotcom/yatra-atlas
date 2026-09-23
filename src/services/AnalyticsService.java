package services;

import algorithms.analytics.PrefixSum;
import algorithms.analytics.Reduce;
import data.TravelData;
import model.Destination;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.HashMap;
import java.util.Map;

/**
 * Travel Analytics Service powered by Reduce and Prefix Sum.
 * Computes macroeconomic travel metrics, category shares, budget spreads,
 * and cumulative travel stats without exposing algorithmic jargon to customers.
 */
public class AnalyticsService {

    public static class AnalyticsDashboard {
        public int totalDestinations;
        public int totalAttractions;
        public int totalHotels;
        public int totalRestaurants;
        public int totalIntercityRoutes;
        public int minDailyBudget;
        public int maxDailyBudget;
        public double avgDailyBudget;
        public Map<String, Integer> categoryDistribution;
        public int[] sampleItineraryPrefixDistances;
        public double computeTimeMicros;

        public AnalyticsDashboard(int totalDestinations, int totalAttractions, int totalHotels,
                                  int totalRestaurants, int totalIntercityRoutes, int minDailyBudget,
                                  int maxDailyBudget, double avgDailyBudget,
                                  Map<String, Integer> categoryDistribution,
                                  int[] sampleItineraryPrefixDistances, double computeTimeMicros) {
            this.totalDestinations = totalDestinations;
            this.totalAttractions = totalAttractions;
            this.totalHotels = totalHotels;
            this.totalRestaurants = totalRestaurants;
            this.totalIntercityRoutes = totalIntercityRoutes;
            this.minDailyBudget = minDailyBudget;
            this.maxDailyBudget = maxDailyBudget;
            this.avgDailyBudget = avgDailyBudget;
            this.categoryDistribution = categoryDistribution;
            this.sampleItineraryPrefixDistances = sampleItineraryPrefixDistances;
            this.computeTimeMicros = computeTimeMicros;
        }
    }

    /**
     * Aggregates national travel metrics using Reduce operations.
     */
    public static AnalyticsDashboard getPlatformAnalytics() {
        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        Destination[] dests = TravelData.DESTINATIONS;
        int n = dests.length;

        int[] attractionCounts = new int[n];
        int[] hotelCounts = new int[n];
        int[] restaurantCounts = new int[n];
        int[] budgets = new int[n];
        Map<String, Integer> categories = new HashMap<>();

        for (int i = 0; i < n; i++) {
            attractionCounts[i] = dests[i].getAttractions().length;
            hotelCounts[i] = dests[i].getHotels().length;
            restaurantCounts[i] = dests[i].getRestaurants().length;
            budgets[i] = dests[i].getAvgDailyBudget();

            String cat = dests[i].getCategory();
            categories.put(cat, categories.getOrDefault(cat, 0) + 1);
        }

        // Apply Reduce algorithms
        int totalAttractions = Reduce.sum(attractionCounts);
        int totalHotels = Reduce.sum(hotelCounts);
        int totalRestaurants = Reduce.sum(restaurantCounts);
        int minBudget = Reduce.min(budgets);
        int maxBudget = Reduce.max(budgets);
        double avgBudget = Reduce.average(budgets);

        // Apply PrefixSum on a representative Golden Triangle & Coastal itinerary
        int[] sampleLegs = new int[]{ 210, 240, 390, 560, 570 }; // Delhi-Agra-Jaipur-Udaipur-Goa-Bengaluru
        PrefixSum prefixSum = new PrefixSum(sampleLegs);
        int[] prefixDistances = prefixSum.getPrefixArray();

        timer.stop();
        double timeTaken = timer.getElapsedMicros();

        AlgorithmSelector.recordDecision(
            "TRAVEL_ANALYTICS",
            "Reduce (Sum, Min, Max, Avg) & 1D Prefix Sum",
            "Calculated aggregate metrics across " + n + " destinations and " + totalAttractions + " attractions.",
            "O(N) Reduce, O(1) Prefix Query",
            timeTaken
        );

        return new AnalyticsDashboard(
            n, totalAttractions, totalHotels, totalRestaurants,
            TravelData.TRAVEL_EDGES.length / 2, minBudget, maxBudget,
            Math.round(avgBudget * 100.0) / 100.0, categories,
            prefixDistances, timeTaken
        );
    }
}
