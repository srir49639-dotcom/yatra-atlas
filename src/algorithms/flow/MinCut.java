package algorithms.flow;

/**
 * Minimum Cut Algorithm based on the Max-Flow Min-Cut Theorem.
 * Identifies critical bottleneck transit corridors whose capacity restriction limits total flow.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 */
public class MinCut {

    public static class CutEdge {
        public int from;
        public int to;
        public int capacity;

        public CutEdge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }
    }

    public static class MinCutResult {
        public boolean[] sourcePartition; // S partition: true if vertex is reachable from source in residual graph
        public CutEdge[] bottleneckEdges;  // Critical edges crossing S -> T
        public int totalCutCapacity;       // Sum of capacities of cut edges (equals Max Flow)

        public MinCutResult(boolean[] sourcePartition, CutEdge[] bottleneckEdges, int totalCutCapacity) {
            this.sourcePartition = sourcePartition;
            this.bottleneckEdges = bottleneckEdges;
            this.totalCutCapacity = totalCutCapacity;
        }
    }

    /**
     * Finds the minimum cut partition and critical bottleneck corridors.
     */
    public static MinCutResult findMinCut(FlowNetwork network, int source) {
        if (network == null || source < 0 || source >= network.numVertices) {
            return new MinCutResult(new boolean[0], new CutEdge[0], 0);
        }

        int v = network.numVertices;
        boolean[] visited = new boolean[v];

        // BFS traversal in residual network
        int[] queue = new int[v];
        int head = 0, tail = 0;

        queue[tail++] = source;
        visited[source] = true;

        while (head < tail) {
            int u = queue[head++];
            int edgeIdx = network.head[u];
            while (edgeIdx != -1) {
                FlowEdge edge = network.edges[edgeIdx];
                // In residual network, traverse edges with positive residual capacity
                if (edge.residualCapacity() > 0 && !visited[edge.to]) {
                    visited[edge.to] = true;
                    queue[tail++] = edge.to;
                }
                edgeIdx = edge.next;
            }
        }

        // Find all original edges going from visited (S) to unvisited (T)
        CutEdge[] tempCut = new CutEdge[network.edgeCount];
        int cutCount = 0;
        int totalCap = 0;

        for (int i = 0; i < network.edgeCount; i += 2) { // Original forward edges are at even indices
            FlowEdge edge = network.edges[i];
            if (visited[edge.from] && !visited[edge.to]) {
                tempCut[cutCount++] = new CutEdge(edge.from, edge.to, edge.capacity);
                totalCap += edge.capacity;
            }
        }

        CutEdge[] finalCut = new CutEdge[cutCount];
        for (int i = 0; i < cutCount; i++) {
            finalCut[i] = tempCut[i];
        }

        return new MinCutResult(visited, finalCut, totalCap);
    }
}
