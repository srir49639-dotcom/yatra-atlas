package algorithms.flow;

/**
 * Flow Network representation for transport capacity calculations.
 * Strict DSA-3: Handcrafted adjacency list using primitive arrays, NO java.util.* used.
 */
public class FlowNetwork {
    public int numVertices;
    public int[] head;
    public FlowEdge[] edges;
    public int edgeCount;

    public FlowNetwork(int numVertices, int initialCapacity) {
        this.numVertices = numVertices;
        this.head = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            this.head[i] = -1;
        }
        this.edges = new FlowEdge[initialCapacity > 0 ? initialCapacity : 16];
        this.edgeCount = 0;
    }

    /**
     * Adds a directed transport connection with given daily seat/passenger capacity.
     * Adds forward edge with capacity, and reverse edge with 0 capacity.
     */
    public void addEdge(int from, int to, int capacity) {
        ensureCapacity(edgeCount + 2);

        int fwdIdx = edgeCount++;
        int revIdx = edgeCount++;

        edges[fwdIdx] = new FlowEdge(from, to, capacity, head[from]);
        edges[fwdIdx].residualEdgeIndex = revIdx;
        head[from] = fwdIdx;

        edges[revIdx] = new FlowEdge(to, from, 0, head[to]);
        edges[revIdx].residualEdgeIndex = fwdIdx;
        head[to] = revIdx;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > edges.length) {
            int newCap = edges.length * 2;
            if (newCap < minCapacity) newCap = minCapacity;
            FlowEdge[] newEdges = new FlowEdge[newCap];
            for (int i = 0; i < edgeCount; i++) {
                newEdges[i] = edges[i];
            }
            edges = newEdges;
        }
    }
}
