package services;

import algorithms.randomized.ReservoirSampling;
import data.TravelData;
import model.Destination;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Destination Recommendation Service powered by Reservoir Sampling.
 * Implements "Discover Something New" / "Surprise Me" to select uniformly random
 * destinations and hidden gems in O(N) time with equal probability.
 */
public class RecommendationService {

    public static class DiscoveryResult {
        public Destination destination;
        public String recommendationReason;
        public double selectionTimeMicros;

        public DiscoveryResult(Destination destination, String recommendationReason, double selectionTimeMicros) {
            this.destination = destination;
            this.recommendationReason = recommendationReason;
            this.selectionTimeMicros = selectionTimeMicros;
        }
    }

    /**
     * Selects a random destination using Reservoir Sampling.
     */
    public static DiscoveryResult surpriseMe() {
        int n = TravelData.DESTINATIONS.length;

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        int[] sample = ReservoirSampling.sampleIndices(n, 1);
        int selectedIndex = (sample.length > 0) ? sample[0] : 0;
        Destination d = TravelData.DESTINATIONS[selectedIndex];

        timer.stop();
        double timeTaken = timer.getElapsedMicros();

        AlgorithmSelector.recordDecision(
            "RANDOM_DISCOVERY",
            "Reservoir Sampling (Algorithm R)",
            "Sampled 1 random destination uniformly from " + n + " candidates in single pass O(N) stream.",
            "O(N)",
            timeTaken
        );

        String reason = "Handpicked for its stunning " + d.getCategory().toLowerCase() + " ambiance and rich local experiences.";
        return new DiscoveryResult(d, reason, timeTaken);
    }

    /**
     * Selects k random destinations for curated featured discovery.
     */
    public static List<Destination> getRandomDestinations(int k) {
        int n = TravelData.DESTINATIONS.length;
        int[] indices = ReservoirSampling.sampleIndices(n, k);
        List<Destination> list = new ArrayList<>();
        for (int idx : indices) {
            list.add(TravelData.DESTINATIONS[idx]);
        }
        return list;
    }
}
