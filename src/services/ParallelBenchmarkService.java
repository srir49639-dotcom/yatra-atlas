package services;

import algorithms.string.KMP;
import data.TravelData;
import model.Destination;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Parallel / Scalable Processing Benchmark Service (CO6 / Parallel Scalability).
 * Runs real concurrent versus sequential batch search workloads across destination corpora.
 * Measures real wall-clock execution times, throughput, and multi-core speedup ratios.
 */
public class ParallelBenchmarkService {

    public static class BenchmarkResult {
        public int totalQueriesRun;
        public int corpusLengthChars;
        public int threadCount;
        public double sequentialTimeMillis;
        public double parallelTimeMillis;
        public double speedupFactor;
        public double sequentialThroughputQps;
        public double parallelThroughputQps;

        public BenchmarkResult(int totalQueriesRun, int corpusLengthChars, int threadCount,
                               double sequentialTimeMillis, double parallelTimeMillis,
                               double speedupFactor, double sequentialThroughputQps,
                               double parallelThroughputQps) {
            this.totalQueriesRun = totalQueriesRun;
            this.corpusLengthChars = corpusLengthChars;
            this.threadCount = threadCount;
            this.sequentialTimeMillis = sequentialTimeMillis;
            this.parallelTimeMillis = parallelTimeMillis;
            this.speedupFactor = speedupFactor;
            this.sequentialThroughputQps = sequentialThroughputQps;
            this.parallelThroughputQps = parallelThroughputQps;
        }
    }

    /**
     * Executes real comparative benchmark between sequential and parallel KMP string searching.
     * @param queryCount Number of search iterations to benchmark (e.g. 500 - 2000)
     */
    public static BenchmarkResult runBenchmark(int queryCount) {
        if (queryCount <= 0) queryCount = 800;

        // Build a consolidated corpus of all destinations
        StringBuilder sb = new StringBuilder();
        for (Destination d : TravelData.DESTINATIONS) {
            sb.append(d.getDescription()).append(" ");
            for (var a : d.getAttractions()) sb.append(a.getDescription()).append(" ");
        }
        String corpus = sb.toString();

        String[] testPatterns = new String[] {
            "temple", "palace", "fort", "beach", "mountain", "heritage", "bazaar",
            "unesco", "river", "royal", "sanctuary", "architecture", "expressway"
        };

        // 1. Sequential Run
        PerformanceTimer seqTimer = new PerformanceTimer();
        seqTimer.start();
        int seqMatches = 0;
        for (int i = 0; i < queryCount; i++) {
            String p = testPatterns[i % testPatterns.length];
            int[] matches = KMP.search(corpus, p);
            seqMatches += matches.length;
        }
        seqTimer.stop();
        double seqMillis = Math.max(0.1, seqTimer.getElapsedMillis());

        // 2. Parallel Run using ThreadPool
        int numThreads = Math.max(2, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        PerformanceTimer parTimer = new PerformanceTimer();
        parTimer.start();

        List<Future<Integer>> futures = new ArrayList<>();
        int chunkSize = queryCount / numThreads;

        for (int t = 0; t < numThreads; t++) {
            final int startIdx = t * chunkSize;
            final int endIdx = (t == numThreads - 1) ? queryCount : (t + 1) * chunkSize;

            futures.add(executor.submit(() -> {
                int localMatches = 0;
                for (int i = startIdx; i < endIdx; i++) {
                    String p = testPatterns[i % testPatterns.length];
                    int[] matches = KMP.search(corpus, p);
                    localMatches += matches.length;
                }
                return localMatches;
            }));
        }

        int parMatches = 0;
        for (Future<Integer> f : futures) {
            try {
                parMatches += f.get();
            } catch (Exception e) {
                // handle thread exception
            }
        }
        parTimer.stop();
        executor.shutdown();

        double parMillis = Math.max(0.1, parTimer.getElapsedMillis());
        double speedup = Math.round((seqMillis / parMillis) * 100.0) / 100.0;
        double seqQps = Math.round((queryCount / (seqMillis / 1000.0)) * 10.0) / 10.0;
        double parQps = Math.round((queryCount / (parMillis / 1000.0)) * 10.0) / 10.0;

        return new BenchmarkResult(
            queryCount, corpus.length(), numThreads,
            Math.round(seqMillis * 100.0) / 100.0,
            Math.round(parMillis * 100.0) / 100.0,
            speedup, seqQps, parQps
        );
    }
}
