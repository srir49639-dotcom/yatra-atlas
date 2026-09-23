package algorithms.dp;

/**
 * Bitmask Dynamic Programming for the Travelling Salesperson Problem (Held-Karp Algorithm).
 * Used in "Plan My Trip" to compute the optimal visiting sequence among selected destinations.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(N^2 * 2^N) for exact Held-Karp (N <= 16).
 * Space Complexity: O(N * 2^N) auxiliary.
 */
public class BitmaskTSP {

    public static final int INF = 1000000000;

    public static class TSPResult {
        public int[] tour;             // ordered indices of visited destinations
        public int totalDistance;      // minimum path cost
        public boolean isOptimal;      // true if solved via exact Bitmask DP

        public TSPResult(int[] tour, int totalDistance, boolean isOptimal) {
            this.tour = tour;
            this.totalDistance = totalDistance;
            this.isOptimal = isOptimal;
        }
    }

    /**
     * Solves TSP starting at origin (index 0) visiting all other vertices exactly once.
     * @param distMatrix n x n symmetric or asymmetric distance matrix
     * @return TSPResult with optimal tour order and minimum distance
     */
    public static TSPResult solve(int[][] distMatrix) {
        if (distMatrix == null || distMatrix.length == 0) {
            return new TSPResult(new int[0], 0, true);
        }

        int n = distMatrix.length;
        if (n == 1) {
            return new TSPResult(new int[]{0}, 0, true);
        }
        if (n == 2) {
            return new TSPResult(new int[]{0, 1}, distMatrix[0][1], true);
        }

        // For large n (> 16), fallback to Nearest-Neighbor heuristic to preserve memory and guarantee O(N^2)
        if (n > 16) {
            return solveNearestNeighbor(distMatrix);
        }

        int numStates = 1 << n;
        int[][] dp = new int[numStates][n];
        int[][] parent = new int[numStates][n];

        for (int mask = 0; mask < numStates; mask++) {
            for (int u = 0; u < n; u++) {
                dp[mask][u] = INF;
                parent[mask][u] = -1;
            }
        }

        // Base case: starting at vertex 0 with only vertex 0 visited (mask = 1)
        dp[1][0] = 0;

        for (int mask = 1; mask < numStates; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0 || dp[mask][u] >= INF) continue;

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0) { // vertex v not yet visited
                        int nextMask = mask | (1 << v);
                        int newDist = dp[mask][u] + distMatrix[u][v];

                        if (newDist < dp[nextMask][v]) {
                            dp[nextMask][v] = newDist;
                            parent[nextMask][v] = u;
                        }
                    }
                }
            }
        }

        // Find the best ending vertex when all vertices are visited (full mask = (1 << n) - 1)
        int fullMask = (1 << n) - 1;
        int bestEnd = -1;
        int minCost = INF;

        for (int u = 1; u < n; u++) {
            if (dp[fullMask][u] < minCost) {
                minCost = dp[fullMask][u];
                bestEnd = u;
            }
        }

        if (bestEnd == -1) {
            // In case of disconnected graph, fallback gracefully
            return solveNearestNeighbor(distMatrix);
        }

        // Reconstruct path backwards
        int[] tour = new int[n];
        int currMask = fullMask;
        int currNode = bestEnd;

        for (int step = n - 1; step >= 0; step--) {
            tour[step] = currNode;
            int prevNode = parent[currMask][currNode];
            currMask ^= (1 << currNode);
            currNode = prevNode;
        }

        return new TSPResult(tour, minCost, true);
    }

    /**
     * Nearest Neighbor heuristic fallback for large N (> 16).
     */
    public static TSPResult solveNearestNeighbor(int[][] distMatrix) {
        int n = distMatrix.length;
        int[] tour = new int[n];
        boolean[] visited = new boolean[n];

        tour[0] = 0;
        visited[0] = true;
        int totalDist = 0;

        for (int step = 1; step < n; step++) {
            int last = tour[step - 1];
            int bestNext = -1;
            int bestDist = INF;

            for (int candidate = 0; candidate < n; candidate++) {
                if (!visited[candidate] && distMatrix[last][candidate] < bestDist) {
                    bestDist = distMatrix[last][candidate];
                    bestNext = candidate;
                }
            }

            if (bestNext == -1) {
                // If disconnected, pick any unvisited
                for (int candidate = 0; candidate < n; candidate++) {
                    if (!visited[candidate]) {
                        bestNext = candidate;
                        bestDist = distMatrix[last][candidate];
                        break;
                    }
                }
            }

            tour[step] = bestNext;
            visited[bestNext] = true;
            totalDist += (bestDist >= INF ? 0 : bestDist);
        }

        return new TSPResult(tour, totalDist, false);
    }
}
