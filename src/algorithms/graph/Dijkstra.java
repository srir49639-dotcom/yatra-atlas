package algorithms.graph;

/**
 * Dijkstra's Single-Source Shortest Path Algorithm.
 * Computes the shortest travel path, minimum travel time, and cumulative cost.
 * Implements a handcrafted indexed binary min-heap for optimal O((V + E) log V) complexity.
 * Strict DSA-3: Pure array operations, NO java.util.* used.
 */
public class Dijkstra {

    public static final int INF = 1000000000;

    /**
     * Adjacency list edge representation without java.util.*
     */
    public static class Edge {
        public int to;
        public int weight;     // distance in km or time in minutes
        public int cost;       // monetary cost in INR
        public int next;       // index of next edge in custom linked list

        public Edge(int to, int weight, int cost, int next) {
            this.to = to;
            this.weight = weight;
            this.cost = cost;
            this.next = next;
        }
    }

    public static class ShortestPathResult {
        public int[] path;         // sequence of vertex indices from source to destination
        public int totalDistance;  // total distance or metric value
        public int totalCost;      // monetary cost along the path
        public boolean reachable;  // whether destination is reachable

        public ShortestPathResult(int[] path, int totalDistance, int totalCost, boolean reachable) {
            this.path = path;
            this.totalDistance = totalDistance;
            this.totalCost = totalCost;
            this.reachable = reachable;
        }
    }

    /**
     * Handcrafted indexed Min-Heap for Dijkstra without java.util.*
     */
    private static class MinHeap {
        int[] heap;       // stores vertex IDs
        int[] dist;       // stores current distance values
        int[] pos;        // pos[v] = index of vertex v in heap
        int size;

        MinHeap(int capacity) {
            heap = new int[capacity];
            dist = new int[capacity];
            pos = new int[capacity];
            size = 0;
            for (int i = 0; i < capacity; i++) pos[i] = -1;
        }

        void insertOrDecreaseKey(int v, int d) {
            dist[v] = d;
            if (pos[v] == -1) {
                pos[v] = size;
                heap[size] = v;
                bubbleUp(size);
                size++;
            } else {
                bubbleUp(pos[v]);
            }
        }

        int extractMin() {
            if (size == 0) return -1;
            int minVertex = heap[0];
            int lastVertex = heap[size - 1];
            heap[0] = lastVertex;
            pos[lastVertex] = 0;
            pos[minVertex] = -1;
            size--;
            if (size > 0) {
                bubbleDown(0);
            }
            return minVertex;
        }

        boolean isEmpty() {
            return size == 0;
        }

        void bubbleUp(int idx) {
            int current = heap[idx];
            while (idx > 0) {
                int parentIdx = (idx - 1) / 2;
                int parentNode = heap[parentIdx];
                if (dist[current] < dist[parentNode]) {
                    heap[idx] = parentNode;
                    pos[parentNode] = idx;
                    idx = parentIdx;
                } else {
                    break;
                }
            }
            heap[idx] = current;
            pos[current] = idx;
        }

        void bubbleDown(int idx) {
            int current = heap[idx];
            int half = size / 2;
            while (idx < half) {
                int leftChildIdx = 2 * idx + 1;
                int rightChildIdx = leftChildIdx + 1;
                int bestChildIdx = leftChildIdx;

                if (rightChildIdx < size && dist[heap[rightChildIdx]] < dist[heap[leftChildIdx]]) {
                    bestChildIdx = rightChildIdx;
                }

                if (dist[heap[bestChildIdx]] < dist[current]) {
                    heap[idx] = heap[bestChildIdx];
                    pos[heap[bestChildIdx]] = idx;
                    idx = bestChildIdx;
                } else {
                    break;
                }
            }
            heap[idx] = current;
            pos[current] = idx;
        }
    }

    /**
     * Computes the shortest path from source to target.
     * @param numVertices Total number of vertices
     * @param head Array of head edge indices for each vertex (head[u] points to first edge in edges array)
     * @param edges Flat array of Edge objects forming adjacency lists
     * @param source Source vertex ID
     * @param target Destination vertex ID
     */
    public static ShortestPathResult findShortestPath(int numVertices, int[] head, Edge[] edges, int source, int target) {
        int[] dist = new int[numVertices];
        int[] parent = new int[numVertices];
        int[] parentCost = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            dist[i] = INF;
            parent[i] = -1;
            parentCost[i] = 0;
        }

        dist[source] = 0;
        MinHeap minHeap = new MinHeap(numVertices);
        minHeap.insertOrDecreaseKey(source, 0);

        while (!minHeap.isEmpty()) {
            int u = minHeap.extractMin();
            if (u == target) break; // Reached target
            if (dist[u] == INF) break;

            int edgeIdx = head[u];
            while (edgeIdx != -1) {
                Edge e = edges[edgeIdx];
                int v = e.to;
                int newDist = dist[u] + e.weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    parent[v] = u;
                    parentCost[v] = e.cost;
                    minHeap.insertOrDecreaseKey(v, newDist);
                }
                edgeIdx = e.next;
            }
        }

        if (dist[target] >= INF) {
            return new ShortestPathResult(new int[0], INF, 0, false);
        }

        // Reconstruct path
        int curr = target;
        int count = 0;
        while (curr != -1) {
            count++;
            curr = parent[curr];
        }

        int[] path = new int[count];
        curr = target;
        int totalCost = 0;
        for (int i = count - 1; i >= 0; i--) {
            path[i] = curr;
            totalCost += parentCost[curr];
            curr = parent[curr];
        }

        return new ShortestPathResult(path, dist[target], totalCost, true);
    }
}
