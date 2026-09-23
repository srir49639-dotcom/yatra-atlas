package algorithms.flow;

/**
 * Edmonds-Karp Maximum Flow Algorithm.
 * Computes maximum passenger/seat transport throughput between origin and destination hubs.
 * Uses BFS to iteratively find shortest augmenting paths in the residual graph.
 * Strict DSA-3: Handcrafted FIFO queue using circular array, NO java.util.* used.
 * Time Complexity: O(V * E^2), Space Complexity: O(V + E) auxiliary.
 */
public class EdmondsKarp {

    public static class MaxFlowResult {
        public int maxFlow;
        public int augmentingPathsCount;
        public FlowNetwork network;

        public MaxFlowResult(int maxFlow, int augmentingPathsCount, FlowNetwork network) {
            this.maxFlow = maxFlow;
            this.augmentingPathsCount = augmentingPathsCount;
            this.network = network;
        }
    }

    /**
     * Handcrafted FIFO Queue for integer vertices without java.util.*
     */
    private static class IntQueue {
        int[] data;
        int head = 0;
        int tail = 0;
        int count = 0;

        IntQueue(int capacity) {
            data = new int[capacity];
        }

        void enqueue(int item) {
            data[tail] = item;
            tail = (tail + 1) % data.length;
            count++;
        }

        int dequeue() {
            int item = data[head];
            head = (head + 1) % data.length;
            count--;
            return item;
        }

        boolean isEmpty() {
            return count == 0;
        }
    }

    /**
     * Computes maximum transport flow from source hub to sink hub.
     */
    public static MaxFlowResult computeMaxFlow(FlowNetwork network, int source, int sink) {
        if (network == null || source < 0 || sink < 0 || source >= network.numVertices || sink >= network.numVertices) {
            return new MaxFlowResult(0, 0, network);
        }

        int maxFlow = 0;
        int pathCount = 0;
        int v = network.numVertices;

        int[] parentEdge = new int[v];

        while (true) {
            for (int i = 0; i < v; i++) {
                parentEdge[i] = -1;
            }

            IntQueue queue = new IntQueue(v);
            queue.enqueue(source);
            parentEdge[source] = -2; // Mark visited

            while (!queue.isEmpty()) {
                int u = queue.dequeue();
                if (u == sink) break;

                int edgeIdx = network.head[u];
                while (edgeIdx != -1) {
                    FlowEdge edge = network.edges[edgeIdx];
                    if (edge.residualCapacity() > 0 && parentEdge[edge.to] == -1) {
                        parentEdge[edge.to] = edgeIdx;
                        queue.enqueue(edge.to);
                    }
                    edgeIdx = edge.next;
                }
            }

            // If sink was not reached, augmenting path does not exist
            if (parentEdge[sink] == -1) {
                break;
            }

            // Find bottleneck capacity along the augmenting path
            int bottleneck = Integer.MAX_VALUE;
            int curr = sink;
            while (curr != source) {
                int edgeIdx = parentEdge[curr];
                FlowEdge edge = network.edges[edgeIdx];
                if (edge.residualCapacity() < bottleneck) {
                    bottleneck = edge.residualCapacity();
                }
                curr = edge.from;
            }

            // Augment flow along the path
            curr = sink;
            while (curr != source) {
                int edgeIdx = parentEdge[curr];
                FlowEdge forwardEdge = network.edges[edgeIdx];
                FlowEdge reverseEdge = network.edges[forwardEdge.residualEdgeIndex];

                forwardEdge.flow += bottleneck;
                reverseEdge.flow -= bottleneck;

                curr = forwardEdge.from;
            }

            maxFlow += bottleneck;
            pathCount++;
        }

        return new MaxFlowResult(maxFlow, pathCount, network);
    }
}
