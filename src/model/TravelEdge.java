package model;

/**
 * Represents a directed or undirected travel connection between two destinations.
 * Used for Dijkstra shortest path and Edmonds-Karp capacity networks.
 */
public class TravelEdge {
    private String fromId;
    private String toId;
    private int distanceKm;
    private int travelTimeMinutes;
    private int costInr;
    private int capacitySeatsDaily;
    private String mode;           // Flight, Train, Bus, Highway

    public TravelEdge(String fromId, String toId, int distanceKm, int travelTimeMinutes,
                      int costInr, int capacitySeatsDaily, String mode) {
        this.fromId = fromId;
        this.toId = toId;
        this.distanceKm = distanceKm;
        this.travelTimeMinutes = travelTimeMinutes;
        this.costInr = costInr;
        this.capacitySeatsDaily = capacitySeatsDaily;
        this.mode = mode;
    }

    public String getFromId() { return fromId; }
    public String getToId() { return toId; }
    public int getDistanceKm() { return distanceKm; }
    public int getTravelTimeMinutes() { return travelTimeMinutes; }
    public int getCostInr() { return costInr; }
    public int getCapacitySeatsDaily() { return capacitySeatsDaily; }
    public String getMode() { return mode; }
}
