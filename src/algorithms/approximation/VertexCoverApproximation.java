package algorithms.approximation;

/**
 * Vertex Cover 2-Approximation Algorithm.
 * Solves the NP-hard Minimum Vertex Cover problem within a provable approximation ratio of 2.
 * Real-world application: "Travel Network Optimizer" identifies strategic regional gateway hubs
 * that cover every intercity connection corridor.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 * Time Complexity: O(V + E), Space Complexity: O(V + E) auxiliary.
 */
public class VertexCoverApproximation {

    public static class EdgePair {
        public int u;
        public int v;

        public EdgePair(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    public static class VertexCoverResult {
        public int[] coverVertices;        // Selected hub indices in the vertex cover
        public EdgePair[] matchingEdges;   // Edges selected in the maximal matching
        public int totalVerticesCovered;
        public double approximationRatio;  // Guaranteed <= 2.0

        public VertexCoverResult(int[] coverVertices, EdgePair[] matchingEdges, int totalVerticesCovered) {
            this.coverVertices = coverVertices;
            this.matchingEdges = matchingEdges;
            this.totalVerticesCovered = totalVerticesCovered;
            this.approximationRatio = 2.0;
        }
    }

    /**
     * Computes a 2-approximation of the Minimum Vertex Cover using maximal matching.
     * @param numVertices Total vertices in network
     * @param edges Flat list of undirected edges (pairs of [u, v])
     */
    public static VertexCoverResult approximateVertexCover(int numVertices, EdgePair[] edges) {
        if (numVertices <= 0 || edges == null || edges.length == 0) {
            return new VertexCoverResult(new int[0], new EdgePair[0], 0);
        }

        boolean[] inCover = new boolean[numVertices];
        EdgePair[] matching = new EdgePair[edges.length];
        int matchingCount = 0;

        // Iterate through edges. If neither endpoint is in cover, add both endpoints
        for (int i = 0; i < edges.length; i++) {
            EdgePair edge = edges[i];
            int u = edge.u;
            int v = edge.v;

            if (!inCover[u] && !inCover[v]) {
                inCover[u] = true;
                inCover[v] = true;
                matching[matchingCount++] = edge;
            }
        }

        // Count selected vertices
        int count = 0;
        for (int i = 0; i < numVertices; i++) {
            if (inCover[i]) count++;
        }

        int[] cover = new int[count];
        int idx = 0;
        for (int i = 0; i < numVertices; i++) {
            if (inCover[i]) {
                cover[idx++] = i;
            }
        }

        EdgePair[] finalMatching = new EdgePair[matchingCount];
        for (int i = 0; i < matchingCount; i++) {
            finalMatching[i] = matching[i];
        }

        return new VertexCoverResult(cover, finalMatching, count);
    }
}
