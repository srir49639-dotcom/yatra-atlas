package services;

import algorithms.approximation.VertexCoverApproximation;
import algorithms.flow.EdmondsKarp;
import algorithms.flow.FlowNetwork;
import algorithms.flow.MinCut;
import data.TravelData;
import model.Destination;
import model.TravelEdge;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Transport Capacity & Network Optimization Service.
 * Implements:
 * 1. Edmonds-Karp Maximum Flow: Computes daily passenger transit capacity between hubs.
 * 2. Minimum Cut: Identifies critical bottleneck transit corridors limiting network throughput.
 * 3. Vertex Cover 2-Approximation: Identifies strategic gateway hubs covering all transit routes.
 */
public class TransportService {

    public static class BottleneckCorridor {
        public String fromCity;
        public String toCity;
        public int capacitySeats;
        public String impactNote;

        public BottleneckCorridor(String fromCity, String toCity, int capacitySeats, String impactNote) {
            this.fromCity = fromCity;
            this.toCity = toCity;
            this.capacitySeats = capacitySeats;
            this.impactNote = impactNote;
        }
    }

    public static class CapacityAnalysisResult {
        public String sourceCity;
        public String sinkCity;
        public int maxDailySeatCapacity;
        public int augmentingPathsCount;
        public List<BottleneckCorridor> criticalCorridors;
        public String capacityStatus; // "Optimal Flow", "High Congestion / Constrained", "Moderate Throughput"
        public double computationTimeMicros;

        public CapacityAnalysisResult(String sourceCity, String sinkCity, int maxDailySeatCapacity,
                                      int augmentingPathsCount, List<BottleneckCorridor> criticalCorridors,
                                      String capacityStatus, double computationTimeMicros) {
            this.sourceCity = sourceCity;
            this.sinkCity = sinkCity;
            this.maxDailySeatCapacity = maxDailySeatCapacity;
            this.augmentingPathsCount = augmentingPathsCount;
            this.criticalCorridors = criticalCorridors;
            this.capacityStatus = capacityStatus;
            this.computationTimeMicros = computationTimeMicros;
        }
    }

    public static class NetworkOptimizerResult {
        public List<String> strategicHubs;
        public int totalCoveredConnections;
        public double approximationGuarantee;
        public String description;
        public double timeTakenMicros;

        public NetworkOptimizerResult(List<String> strategicHubs, int totalCoveredConnections,
                                      double approximationGuarantee, String description, double timeTakenMicros) {
            this.strategicHubs = strategicHubs;
            this.totalCoveredConnections = totalCoveredConnections;
            this.approximationGuarantee = approximationGuarantee;
            this.description = description;
            this.timeTakenMicros = timeTakenMicros;
        }
    }

    /**
     * Computes transport capacity and critical bottleneck corridors between two cities using Edmonds-Karp and Min-Cut.
     */
    public static CapacityAnalysisResult analyzeCapacity(String sourceId, String sinkId) {
        Destination src = TravelData.getDestinationById(sourceId);
        Destination snk = TravelData.getDestinationById(sinkId);
        if (src == null || snk == null) {
            return new CapacityAnalysisResult("Unknown", "Unknown", 0, 0, new ArrayList<>(), "Unavailable", 0);
        }

        int s = TravelData.getDestinationIndex(sourceId);
        int t = TravelData.getDestinationIndex(sinkId);
        int v = TravelData.DESTINATIONS.length;

        FlowNetwork network = new FlowNetwork(v, TravelData.TRAVEL_EDGES.length * 2);

        for (TravelEdge edge : TravelData.TRAVEL_EDGES) {
            int u = TravelData.getDestinationIndex(edge.getFromId());
            int w = TravelData.getDestinationIndex(edge.getToId());
            if (u != -1 && w != -1) {
                network.addEdge(u, w, edge.getCapacitySeatsDaily());
            }
        }

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        EdmondsKarp.MaxFlowResult flowResult = EdmondsKarp.computeMaxFlow(network, s, t);
        MinCut.MinCutResult cutResult = MinCut.findMinCut(flowResult.network, s);

        timer.stop();
        double timeTaken = timer.getElapsedMicros();

        List<BottleneckCorridor> bottlenecks = new ArrayList<>();
        for (MinCut.CutEdge ce : cutResult.bottleneckEdges) {
            Destination from = TravelData.DESTINATIONS[ce.from];
            Destination to = TravelData.DESTINATIONS[ce.to];
            bottlenecks.add(new BottleneckCorridor(
                from.getName(), to.getName(), ce.capacity,
                "Critical bottleneck edge restricting regional throughput to " + ce.capacity + " seats/day."
            ));
        }

        String status = flowResult.maxFlow > 8000 ? "High Throughput Corridor" :
                        flowResult.maxFlow > 4000 ? "Moderate Capacity - Advance Booking Advised" :
                        "High Congestion / Peak Constrained";

        AlgorithmSelector.recordDecision(
            "TRANSPORT_CAPACITY",
            "Edmonds-Karp Max-Flow & Minimum-Cut",
            "Evaluated max transit capacity between " + src.getName() + " and " + snk.getName() + ". Max flow: " + flowResult.maxFlow + " seats/day across " + flowResult.augmentingPathsCount + " augmenting paths.",
            "O(V * E^2)",
            timeTaken
        );

        return new CapacityAnalysisResult(src.getName(), snk.getName(), flowResult.maxFlow, flowResult.augmentingPathsCount, bottlenecks, status, timeTaken);
    }

    /**
     * Travel Network Optimizer using Vertex Cover 2-Approximation.
     * Determines the strategic hub cities necessary to cover all intercity travel connections.
     */
    public static NetworkOptimizerResult optimizeNetworkHubs() {
        int v = TravelData.DESTINATIONS.length;
        List<VertexCoverApproximation.EdgePair> pairList = new ArrayList<>();

        for (TravelEdge e : TravelData.TRAVEL_EDGES) {
            int u = TravelData.getDestinationIndex(e.getFromId());
            int w = TravelData.getDestinationIndex(e.getToId());
            if (u != -1 && w != -1 && u < w) { // Avoid duplicate undirected edges
                pairList.add(new VertexCoverApproximation.EdgePair(u, w));
            }
        }

        VertexCoverApproximation.EdgePair[] edgePairs = pairList.toArray(new VertexCoverApproximation.EdgePair[0]);

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        VertexCoverApproximation.VertexCoverResult result = VertexCoverApproximation.approximateVertexCover(v, edgePairs);

        timer.stop();
        double timeTaken = timer.getElapsedMicros();

        List<String> hubNames = new ArrayList<>();
        for (int idx : result.coverVertices) {
            hubNames.add(TravelData.DESTINATIONS[idx].getName());
        }

        AlgorithmSelector.recordDecision(
            "NETWORK_OPTIMIZATION",
            "Vertex Cover 2-Approximation (Maximal Matching)",
            "Identified " + hubNames.size() + " strategic gateway hubs covering all " + edgePairs.length + " transit routes.",
            "O(V + E)",
            timeTaken
        );

        String desc = "These " + hubNames.size() + " strategic hub cities serve as optimal gateway nodes covering all intercity transit corridors across the national network.";

        return new NetworkOptimizerResult(hubNames, edgePairs.length, result.approximationRatio, desc, timeTaken);
    }
}
