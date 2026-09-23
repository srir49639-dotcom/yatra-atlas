package algorithms.flow;

/**
 * Directed residual flow edge for transport networks.
 * Strict DSA-3: Pure object fields, NO java.util.* used.
 */
public class FlowEdge {
    public int from;
    public int to;
    public int capacity;
    public int flow;
    public int residualEdgeIndex; // Index of the reverse edge in network
    public int next;              // Next edge index in adjacency list

    public FlowEdge(int from, int to, int capacity, int next) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0;
        this.next = next;
    }

    public int residualCapacity() {
        return capacity - flow;
    }
}
