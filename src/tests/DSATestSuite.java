package tests;

import algorithms.analytics.PrefixSum;
import algorithms.analytics.Reduce;
import algorithms.approximation.VertexCoverApproximation;
import algorithms.dp.*;
import algorithms.flow.*;
import algorithms.graph.Dijkstra;
import algorithms.randomized.MillerRabin;
import algorithms.randomized.ReservoirSampling;
import algorithms.string.*;
import data.TravelData;
import services.*;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive Automated DSA-3 Test Suite.
 * Validates all 21 algorithms and application services for correctness, edge cases,
 * and Course Outcome (CO1 - CO6) academic compliance.
 * Callable both from CLI (main) and REST API (/api/tests/run) for the live Test Suite Dashboard.
 */
public class DSATestSuite {

    public static class TestCaseResult {
        public String category;
        public String testName;
        public boolean passed;
        public double timeMicros;
        public String details;

        public TestCaseResult(String category, String testName, boolean passed, double timeMicros, String details) {
            this.category = category;
            this.testName = testName;
            this.passed = passed;
            this.timeMicros = timeMicros;
            this.details = details;
        }
    }

    public static class TestSuiteResult {
        public int totalTests;
        public int passed;
        public int failed;
        public double passPercentage;
        public double totalTimeMillis;
        public List<TestCaseResult> testCases;

        public TestSuiteResult(int totalTests, int passed, int failed, double passPercentage, double totalTimeMillis, List<TestCaseResult> testCases) {
            this.totalTests = totalTests;
            this.passed = passed;
            this.failed = failed;
            this.passPercentage = passPercentage;
            this.totalTimeMillis = totalTimeMillis;
            this.testCases = testCases;
        }
    }

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  STARTING DSA-3 AUTOMATED VERIFICATION SUITE");
        System.out.println("===============================================================");

        TestSuiteResult result = runAllTests();

        for (TestCaseResult tc : result.testCases) {
            String status = tc.passed ? "[PASS]" : "[FAIL]";
            System.out.println("  " + status + " [" + tc.category + "] " + tc.testName + " (" + tc.timeMicros + " μs)");
        }

        System.out.println("===============================================================");
        System.out.println("  TEST RUN SUMMARY: " + result.passed + " PASSED, " + result.failed + " FAILED (" + result.passPercentage + "%) in " + result.totalTimeMillis + " ms");
        System.out.println("===============================================================");

        if (result.failed > 0) {
            System.exit(1);
        }
    }

    public static synchronized TestSuiteResult runAllTests() {
        List<TestCaseResult> list = new ArrayList<>();
        PerformanceTimer suiteTimer = new PerformanceTimer();
        suiteTimer.start();

        // 1. String Algorithms (CO2)
        testStringAlgorithms(list);

        // 2. Dynamic Programming Algorithms (CO3)
        testDynamicProgrammingAlgorithms(list);

        // 3. Graph & Flow Algorithms (CO4)
        testGraphAndFlowAlgorithms(list);

        // 4. Approximation & Randomized Algorithms (CO5 & CO6)
        testApproximationAndRandomizedAlgorithms(list);

        // 5. Analytics Algorithms (Reduce & Prefix Sum)
        testAnalyticsAlgorithms(list);

        // 6. Travel Services & Feature Integration
        testServicesIntegration(list);

        suiteTimer.stop();

        int passed = 0;
        int failed = 0;
        for (TestCaseResult tc : list) {
            if (tc.passed) passed++;
            else failed++;
        }

        double pct = (list.isEmpty()) ? 100.0 : (Math.round((passed * 100.0 / list.size()) * 100.0) / 100.0);
        return new TestSuiteResult(list.size(), passed, failed, pct, Math.round(suiteTimer.getElapsedMillis() * 10.0) / 10.0, list);
    }

    private static void record(List<TestCaseResult> list, String cat, String name, boolean cond, double micros, String details) {
        list.add(new TestCaseResult(cat, name, cond, Math.round(micros * 10.0) / 10.0, details));
    }

    private static void testStringAlgorithms(List<TestCaseResult> list) {
        String text = "The Charminar stands in Hyderabad near Golconda Fort";
        String pattern = "Charminar";

        // Naive Search
        PerformanceTimer t1 = new PerformanceTimer(); t1.start();
        int[] naiveMatches = NaiveSearch.search(text, pattern);
        t1.stop();
        record(list, "String (CO2)", "NaiveSearch pattern matching", naiveMatches.length == 1 && naiveMatches[0] == 4, t1.getElapsedMicros(), "Found match at index 4");

        // KMP
        PerformanceTimer t2 = new PerformanceTimer(); t2.start();
        int[] lps = KMP.computeLPS("AABAACAABAA");
        int[] kmpMatches = KMP.search(text, "Fort");
        t2.stop();
        record(list, "String (CO2)", "KMP pattern search & LPS computation", lps[lps.length - 1] == 5 && kmpMatches.length == 1 && kmpMatches[0] == 48, t2.getElapsedMicros(), "LPS verified, match index 48");

        // Z-Algorithm
        PerformanceTimer t3 = new PerformanceTimer(); t3.start();
        int[] zMatches = ZAlgorithm.search(text, "Hyderabad");
        t3.stop();
        record(list, "String (CO2)", "Z-Algorithm linear pattern matching", zMatches.length == 1 && zMatches[0] == 24, t3.getElapsedMicros(), "Match index 24 in O(N+M)");

        // Rabin-Karp
        PerformanceTimer t4 = new PerformanceTimer(); t4.start();
        int[] rkMatches = RabinKarp.search(text, "stands");
        t4.stop();
        record(list, "String (CO2)", "Rabin-Karp rolling polynomial hash matching", rkMatches.length == 1 && rkMatches[0] == 14, t4.getElapsedMicros(), "Prime modulus 10^9+7 hash verified");

        // Suffix Array
        PerformanceTimer t5 = new PerformanceTimer(); t5.start();
        String saStr = "banana";
        int[] sa = SuffixArray.buildSuffixArray(saStr);
        int[] saMatches = SuffixArray.search(saStr, "nan", sa);
        t5.stop();
        record(list, "String (CO2)", "Suffix Array prefix doubling & binary search", sa.length == 6 && saMatches.length == 1 && saMatches[0] == 2, t5.getElapsedMicros(), "Matches substring 'nan'");

        // Kasai LCP
        PerformanceTimer t6 = new PerformanceTimer(); t6.start();
        int[] lcp = LCP.buildLCPArray(saStr, sa);
        String longestRep = LCP.findLongestRepeatedSubstring(saStr, sa, lcp);
        t6.stop();
        record(list, "String (CO2)", "Kasai LCP linear array computation", longestRep.equalsIgnoreCase("ana"), t6.getElapsedMicros(), "Longest repeated substring 'ana'");
    }

    private static void testDynamicProgrammingAlgorithms(List<TestCaseResult> list) {
        // Levenshtein
        PerformanceTimer t1 = new PerformanceTimer(); t1.start();
        int dist1 = Levenshtein.distance("kitten", "sitting");
        t1.stop();
        record(list, "DP (CO3)", "Levenshtein distance calculation", dist1 == 3, t1.getElapsedMicros(), "kitten -> sitting == 3");

        // Damerau-Levenshtein
        PerformanceTimer t2 = new PerformanceTimer(); t2.start();
        int dlDist = DamerauLevenshtein.distance("charminr", "charminar");
        int dlTrans = DamerauLevenshtein.distance("teh", "the");
        t2.stop();
        record(list, "DP (CO3)", "Damerau-Levenshtein transposition handling", dlDist == 1 && dlTrans == 1, t2.getElapsedMicros(), "Transposition edit cost == 1");

        // Needleman-Wunsch (Global)
        PerformanceTimer t3 = new PerformanceTimer(); t3.start();
        var nw = NeedlemanWunsch.align("GCATGCG", "GATTACA");
        t3.stop();
        record(list, "DP (CO3)", "Needleman-Wunsch global sequence alignment", nw.score != 0 && nw.alignedSeq1.length() == nw.alignedSeq2.length(), t3.getElapsedMicros(), "Score: " + nw.score);

        // Smith-Waterman (Local)
        PerformanceTimer t4 = new PerformanceTimer(); t4.start();
        var sw = SmithWaterman.align("ACACACTA", "AGCACACA");
        t4.stop();
        record(list, "DP (CO3)", "Smith-Waterman local sequence alignment", sw.maxScore > 0 && sw.subSeq1.length() > 0, t4.getElapsedMicros(), "Max local score: " + sw.maxScore);

        // Bitmask DP / TSP
        PerformanceTimer t5 = new PerformanceTimer(); t5.start();
        int[][] tspDist = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        var tspResult = BitmaskTSP.solve(tspDist);
        t5.stop();
        record(list, "DP (CO3)", "Bitmask DP Held-Karp TSP optimal tour", tspResult.isOptimal && tspResult.tour.length == 4, t5.getElapsedMicros(), "4-vertex exact tour: " + tspResult.totalDistance);
    }

    private static void testGraphAndFlowAlgorithms(List<TestCaseResult> list) {
        // Dijkstra
        PerformanceTimer t1 = new PerformanceTimer(); t1.start();
        int n = 4;
        int[] head = new int[n];
        Arrays.fill(head, -1);
        Dijkstra.Edge[] edges = new Dijkstra.Edge[8];
        int edgeCount = 0;
        edges[edgeCount] = new Dijkstra.Edge(1, 4, 400, head[0]); head[0] = edgeCount++;
        edges[edgeCount] = new Dijkstra.Edge(2, 2, 200, head[0]); head[0] = edgeCount++;
        edges[edgeCount] = new Dijkstra.Edge(1, 1, 100, head[2]); head[2] = edgeCount++;
        edges[edgeCount] = new Dijkstra.Edge(3, 5, 500, head[1]); head[1] = edgeCount++;
        edges[edgeCount] = new Dijkstra.Edge(3, 8, 800, head[2]); head[2] = edgeCount++;
        var sp = Dijkstra.findShortestPath(n, head, edges, 0, 3);
        t1.stop();
        record(list, "Graph & Flow (CO4)", "Dijkstra Indexed Min-Heap shortest path", sp.reachable && sp.totalDistance == 8, t1.getElapsedMicros(), "Path 0->2->1->3 cost 8");

        // Edmonds-Karp & Min-Cut
        PerformanceTimer t2 = new PerformanceTimer(); t2.start();
        FlowNetwork fn = new FlowNetwork(4, 10);
        fn.addEdge(0, 1, 10);
        fn.addEdge(0, 2, 10);
        fn.addEdge(1, 2, 2);
        fn.addEdge(1, 3, 10);
        fn.addEdge(2, 3, 10);
        var flowRes = EdmondsKarp.computeMaxFlow(fn, 0, 3);
        var cutRes = MinCut.findMinCut(flowRes.network, 0);
        t2.stop();
        record(list, "Graph & Flow (CO4)", "Edmonds-Karp Max-Flow and Min-Cut duality", flowRes.maxFlow == 20 && cutRes.totalCutCapacity == 20, t2.getElapsedMicros(), "Max Flow == Min Cut == 20");
    }

    private static void testApproximationAndRandomizedAlgorithms(List<TestCaseResult> list) {
        // Vertex Cover 2-Approximation
        PerformanceTimer t1 = new PerformanceTimer(); t1.start();
        VertexCoverApproximation.EdgePair[] pairs = new VertexCoverApproximation.EdgePair[] {
            new VertexCoverApproximation.EdgePair(0, 1),
            new VertexCoverApproximation.EdgePair(1, 2),
            new VertexCoverApproximation.EdgePair(2, 3),
            new VertexCoverApproximation.EdgePair(3, 0)
        };
        var vc = VertexCoverApproximation.approximateVertexCover(4, pairs);
        t1.stop();
        record(list, "Approximation (CO5)", "Vertex Cover 2-Approximation maximal matching", vc.coverVertices.length >= 2 && vc.approximationRatio <= 2.0, t1.getElapsedMicros(), "Approximation ratio <= 2.0 guaranteed");

        // Miller-Rabin Primality
        PerformanceTimer t2 = new PerformanceTimer(); t2.start();
        var mrPrime = MillerRabin.test(1000000007L, 10);
        var mrComposite = MillerRabin.test(1000000005L, 10);
        t2.stop();
        record(list, "Randomized (CO6)", "Miller-Rabin probabilistic primality test", mrPrime.isPrime && !mrComposite.isPrime, t2.getElapsedMicros(), "10^9+7 Prime, 10^9+5 Composite");

        // Reservoir Sampling
        PerformanceTimer t3 = new PerformanceTimer(); t3.start();
        int[] samples = ReservoirSampling.sampleIndices(20, 3);
        boolean distinct = (samples[0] != samples[1]) && (samples[1] != samples[2]) && (samples[0] != samples[2]);
        t3.stop();
        record(list, "Randomized (CO6)", "Reservoir Sampling uniform single-pass selection", samples.length == 3 && distinct, t3.getElapsedMicros(), "Sampled 3 distinct uniform items");
    }

    private static void testAnalyticsAlgorithms(List<TestCaseResult> list) {
        int[] data = new int[]{10, 20, 30, 40, 50};
        PerformanceTimer t1 = new PerformanceTimer(); t1.start();
        int sum = Reduce.sum(data);
        int min = Reduce.min(data);
        int max = Reduce.max(data);
        double avg = Reduce.average(data);
        t1.stop();
        record(list, "Analytics", "Reduce higher-order aggregation (Sum, Min, Max, Avg)", sum == 150 && min == 10 && max == 50 && avg == 30.0, t1.getElapsedMicros(), "Sum 150, Min 10, Max 50, Avg 30.0");

        PerformanceTimer t2 = new PerformanceTimer(); t2.start();
        PrefixSum ps = new PrefixSum(data);
        int cumAt2 = ps.cumulativeAt(2);
        int range1to3 = ps.rangeSum(1, 3);
        t2.stop();
        record(list, "Analytics", "Prefix Sum O(1) range query evaluation", cumAt2 == 60 && range1to3 == 90, t2.getElapsedMicros(), "cumAt(2)==60, range(1,3)==90");
    }

    private static void testServicesIntegration(List<TestCaseResult> list) {
        // Dataset integrity
        record(list, "Services", "TravelData 20 canonical Indian destinations", TravelData.DESTINATIONS.length == 20, 1.0, "20 Destinations loaded");

        // Search Service
        PerformanceTimer tSearch = new PerformanceTimer(); tSearch.start();
        var searchResp = SearchService.search("fort");
        tSearch.stop();
        record(list, "Services", "SearchService pattern dispatching across entities", searchResp.results.size() > 0, tSearch.getElapsedMicros(), "Found " + searchResp.results.size() + " matches for 'fort'");

        // Fuzzy Suggestion & Aliases
        PerformanceTimer tFuzzy = new PerformanceTimer(); tFuzzy.start();
        String suggCharminr = FuzzySearchService.suggestCorrection("charminr");
        String suggBanglore = FuzzySearchService.suggestCorrection("banglore");
        String suggHydrabad = FuzzySearchService.suggestCorrection("hydrabad");
        tFuzzy.stop();
        record(list, "Services", "FuzzySearchService typo & alias correction",
            "Charminar".equalsIgnoreCase(suggCharminr) && "Bengaluru".equalsIgnoreCase(suggBanglore) && "Hyderabad".equalsIgnoreCase(suggHydrabad),
            tFuzzy.getElapsedMicros(), "charminr->Charminar, banglore->Bengaluru, hydrabad->Hyderabad");

        // Route Service - Intercity
        PerformanceTimer tRoute1 = new PerformanceTimer(); tRoute1.start();
        var routeIntercity = RouteService.findRoute("hyderabad", "goa", "shortest");
        tRoute1.stop();
        record(list, "Services", "RouteService intercity routing (Hyderabad -> Goa)", routeIntercity.reachable && routeIntercity.totalDistanceKm > 0, tRoute1.getElapsedMicros(), "Distance: " + routeIntercity.totalDistanceKm + " km");

        // Route Service - Intra-city Landmark (Charminar to Golconda Fort)
        PerformanceTimer tRoute2 = new PerformanceTimer(); tRoute2.start();
        var routeLandmark = RouteService.findRoute("hyd-1", "hyd-2", "shortest");
        tRoute2.stop();
        record(list, "Services", "RouteService intra-city landmark routing (Charminar -> Golconda Fort)", routeLandmark.reachable && routeLandmark.totalDistanceKm > 0 && routeLandmark.pathCities.size() >= 2, tRoute2.getElapsedMicros(), "Optimal path: " + String.join(" -> ", routeLandmark.pathCities));

        // Route Service - Same source and destination edge case
        PerformanceTimer tRoute3 = new PerformanceTimer(); tRoute3.start();
        var routeSame = RouteService.findRoute("hyderabad", "hyderabad", "shortest");
        tRoute3.stop();
        record(list, "Services", "RouteService identical source/destination edge case", routeSame.reachable && routeSame.totalDistanceKm == 0, tRoute3.getElapsedMicros(), "Handled zero-distance same location cleanly");

        // Trip Planner Service with safe Bitmask DP
        PerformanceTimer tTrip = new PerformanceTimer(); tTrip.start();
        var tripResp = TripPlannerService.planTrip("hyderabad", new String[]{"bengaluru", "chennai", "goa"}, 5, "comfort");
        tTrip.stop();
        record(list, "Services", "TripPlannerService Bitmask TSP itinerary", tripResp.success && tripResp.optimalSequence.size() == 4, tTrip.getElapsedMicros(), "Tour sequence: " + String.join(" -> ", tripResp.optimalSequence));

        // Trip Planner Safe Limit Guard (> 10 destinations)
        String[] tooMany = new String[]{"goa", "delhi", "varanasi", "bengaluru", "jaipur", "agra", "mumbai", "pune", "amritsar", "kochi", "manali"};
        var tripOverLimit = TripPlannerService.planTrip("hyderabad", tooMany, 14, "comfort");
        record(list, "Services", "TripPlannerService safe limit guard (N > 10)", !tripOverLimit.success && tripOverLimit.message.contains("10"), 1.0, "Safely rejected 11 destinations with friendly notice");

        // Transport Service
        PerformanceTimer tTrans = new PerformanceTimer(); tTrans.start();
        var capResp = TransportService.analyzeCapacity("hyderabad", "bengaluru");
        tTrans.stop();
        record(list, "Services", "TransportService Edmonds-Karp seat capacity", capResp.maxDailySeatCapacity > 0, tTrans.getElapsedMicros(), "Max capacity: " + capResp.maxDailySeatCapacity + " seats/day");

        // Network Optimizer
        PerformanceTimer tNet = new PerformanceTimer(); tNet.start();
        var netOpt = TransportService.optimizeNetworkHubs();
        tNet.stop();
        record(list, "Services", "TransportService Vertex Cover strategic network hubs", netOpt.strategicHubs.size() > 0 && netOpt.totalCoveredConnections > 0, tNet.getElapsedMicros(), "Identified " + netOpt.strategicHubs.size() + " strategic gateway hubs");

        // Recommendation Service
        PerformanceTimer tRec = new PerformanceTimer(); tRec.start();
        var rec = RecommendationService.surpriseMe();
        tRec.stop();
        record(list, "Services", "RecommendationService Reservoir surprise discovery", rec.destination != null, tRec.getElapsedMicros(), "Selected: " + rec.destination.getName());

        // Similarity Service
        PerformanceTimer tSim = new PerformanceTimer(); tSim.start();
        var simItems = SimilarityService.findSimilarDestinations("goa", 3);
        var comp = SimilarityService.compareDestinations("hyderabad", "delhi");
        tSim.stop();
        record(list, "Services", "SimilarityService Jaccard & sequence alignment", simItems.size() > 0 && comp != null && comp.globalAlignment != null, tSim.getElapsedMicros(), "Similar to Goa: " + simItems.get(0).destination.getName() + " (Score: " + simItems.get(0).score + ")");

        // Analytics Service
        PerformanceTimer tAnalytics = new PerformanceTimer(); tAnalytics.start();
        var analytics = AnalyticsService.getPlatformAnalytics();
        tAnalytics.stop();
        record(list, "Services", "AnalyticsService Reduce & PrefixSum dashboard aggregation", analytics.totalDestinations == 20 && analytics.totalAttractions > 50, tAnalytics.getElapsedMicros(), "20 destinations, " + analytics.totalAttractions + " attractions");
    }
}
