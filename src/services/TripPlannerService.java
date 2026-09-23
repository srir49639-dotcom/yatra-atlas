package services;

import algorithms.analytics.PrefixSum;
import algorithms.dp.BitmaskTSP;
import data.TravelData;
import model.Destination;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Multi-City Trip Planner Service powered by Bitmask Dynamic Programming for TSP.
 * Calculates the optimal tour sequence among selected destinations.
 * Enriches the planned itinerary with day-by-day cumulative metrics via Prefix Sum.
 */
public class TripPlannerService {

    public static class ItineraryDay {
        public int dayNumber;
        public Destination destination;
        public String action;
        public int dailyTravelKm;
        public int cumulativeKm;
        public int estimatedDailyCost;
        public int cumulativeCost;
        public String highlight;

        public ItineraryDay(int dayNumber, Destination destination, String action,
                            int dailyTravelKm, int cumulativeKm, int estimatedDailyCost,
                            int cumulativeCost, String highlight) {
            this.dayNumber = dayNumber;
            this.destination = destination;
            this.action = action;
            this.dailyTravelKm = dailyTravelKm;
            this.cumulativeKm = cumulativeKm;
            this.estimatedDailyCost = estimatedDailyCost;
            this.cumulativeCost = cumulativeCost;
            this.highlight = highlight;
        }
    }

    public static class TripPlanResponse {
        public String originName;
        public int totalStops;
        public int totalDistanceKm;
        public int totalCostInr;
        public int totalDays;
        public List<String> optimalSequence;
        public List<ItineraryDay> schedule;
        public double calculationTimeMicros;
        public boolean success;
        public String message;

        public TripPlanResponse(String originName, int totalStops, int totalDistanceKm,
                                int totalCostInr, int totalDays, List<String> optimalSequence,
                                List<ItineraryDay> schedule, double calculationTimeMicros) {
            this(originName, totalStops, totalDistanceKm, totalCostInr, totalDays, optimalSequence, schedule, calculationTimeMicros, true, "Optimal itinerary generated via Bitmask DP.");
        }

        public TripPlanResponse(String originName, int totalStops, int totalDistanceKm,
                                int totalCostInr, int totalDays, List<String> optimalSequence,
                                List<ItineraryDay> schedule, double calculationTimeMicros,
                                boolean success, String message) {
            this.originName = originName;
            this.totalStops = totalStops;
            this.totalDistanceKm = totalDistanceKm;
            this.totalCostInr = totalCostInr;
            this.totalDays = totalDays;
            this.optimalSequence = optimalSequence;
            this.schedule = schedule;
            this.calculationTimeMicros = calculationTimeMicros;
            this.success = success;
            this.message = message;
        }
    }

    /**
     * Plans multi-city trip starting from originId and visiting all destinationIds.
     */
    public static TripPlanResponse planTrip(String originId, String[] destinationIds, int days, String preference) {
        if (originId == null || destinationIds == null || destinationIds.length == 0) {
            return new TripPlanResponse("Unknown", 0, 0, 0, days, new ArrayList<>(), new ArrayList<>(), 0, false, "Please specify an origin and at least one destination.");
        }

        // Build list of all cities: index 0 is origin, followed by destinations
        List<Destination> cities = new ArrayList<>();
        Destination origin = TravelData.getDestinationById(originId);
        if (origin == null) return null;
        cities.add(origin);

        for (String id : destinationIds) {
            Destination d = TravelData.getDestinationById(id);
            if (d != null && !d.getId().equalsIgnoreCase(origin.getId()) && !cities.contains(d)) {
                cities.add(d);
            }
        }

        int k = cities.size();
        if (k > 10) {
            return new TripPlanResponse(
                origin.getName(), k, 0, 0, days, new ArrayList<>(), new ArrayList<>(), 0,
                false, "Please select up to 10 destinations for exact trip optimization (Held-Karp Bitmask DP state complexity is O(N² · 2^N), optimal for up to 10 cities)."
            );
        }

        if (k <= 1) {
            List<ItineraryDay> schedule = new ArrayList<>();
            schedule.add(new ItineraryDay(1, origin, "Explore " + origin.getName(), 0, 0, origin.getAvgDailyBudget(), origin.getAvgDailyBudget(), "Full day city exploration"));
            return new TripPlanResponse(origin.getName(), 1, 0, origin.getAvgDailyBudget() * days, days, List.of(origin.getName()), schedule, 0, true, "Single city stay planned.");
        }

        // Build pairwise distance matrix between selected cities using Dijkstra
        int[][] distMatrix = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (i == j) {
                    distMatrix[i][j] = 0;
                } else {
                    var route = RouteService.findRoute(cities.get(i).getId(), cities.get(j).getId(), preference);
                    distMatrix[i][j] = route.reachable && route.totalDistanceKm > 0 ? route.totalDistanceKm : calculateEuclideanApprox(cities.get(i), cities.get(j));
                }
            }
        }

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        // Solve optimal visiting sequence using Bitmask DP TSP
        BitmaskTSP.TSPResult tsp = BitmaskTSP.solve(distMatrix);

        timer.stop();
        double timeTaken = timer.getElapsedMicros();

        AlgorithmSelector.recordDecision(
            "TRIP_PLANNER",
            "Bitmask Dynamic Programming (Held-Karp TSP)",
            "Optimized visiting tour for " + k + " cities with " + (1 << k) + " state spaces. Optimal sequence found.",
            "O(N^2 * 2^N)",
            timeTaken
        );

        List<String> sequence = new ArrayList<>();
        int[] legDistances = new int[tsp.tour.length];
        int[] dailyCosts = new int[tsp.tour.length];

        for (int step = 0; step < tsp.tour.length; step++) {
            int cityIdx = tsp.tour[step];
            Destination d = cities.get(cityIdx);
            sequence.add(d.getName());

            if (step > 0) {
                int prevCityIdx = tsp.tour[step - 1];
                legDistances[step] = distMatrix[prevCityIdx][cityIdx];
            } else {
                legDistances[step] = 0;
            }

            dailyCosts[step] = d.getAvgDailyBudget() + (step > 0 ? (int)(legDistances[step] * 2.5) : 0);
        }

        // Apply PrefixSum for cumulative travel metrics
        PrefixSum distPrefix = new PrefixSum(legDistances);
        PrefixSum costPrefix = new PrefixSum(dailyCosts);

        List<ItineraryDay> schedule = new ArrayList<>();
        int daysPerCity = Math.max(1, days / k);

        for (int step = 0; step < tsp.tour.length; step++) {
            int cityIdx = tsp.tour[step];
            Destination d = cities.get(cityIdx);
            String highlight = d.getAttractions().length > 0 ? "Visit " + d.getAttractions()[0].getName() : "City cultural walk";

            schedule.add(new ItineraryDay(
                step + 1,
                d,
                (step == 0) ? "Arrive & Explore " + d.getName() : "Travel from " + sequence.get(step - 1) + " to " + d.getName(),
                legDistances[step],
                distPrefix.cumulativeAt(step),
                dailyCosts[step],
                costPrefix.cumulativeAt(step),
                highlight
            ));
        }

        int totalCost = costPrefix.cumulativeAt(tsp.tour.length - 1);
        int totalDistance = distPrefix.cumulativeAt(tsp.tour.length - 1);

        return new TripPlanResponse(origin.getName(), k, totalDistance, totalCost, days, sequence, schedule, timeTaken);
    }

    private static int calculateEuclideanApprox(Destination a, Destination b) {
        double dLat = Math.toRadians(b.getLatitude() - a.getLatitude());
        double dLon = Math.toRadians(b.getLongitude() - a.getLongitude());
        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());

        double haversine = Math.sin(dLat/2) * Math.sin(dLat/2) +
                           Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
        return (int) (6371 * c * 1.25); // Road winding factor 1.25
    }
}
