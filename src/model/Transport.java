package model;

/**
 * Represents internal or incoming transportation options for a destination.
 */
public class Transport {
    private String id;
    private String destinationId;
    private String type;           // Flight, Train, Metro, Bus, Taxi, Ferry
    private String provider;
    private String frequency;
    private int avgPrice;
    private String description;

    public Transport(String id, String destinationId, String type, String provider,
                     String frequency, int avgPrice, String description) {
        this.id = id;
        this.destinationId = destinationId;
        this.type = type;
        this.provider = provider;
        this.frequency = frequency;
        this.avgPrice = avgPrice;
        this.description = description;
    }

    public String getId() { return id; }
    public String getDestinationId() { return destinationId; }
    public String getType() { return type; }
    public String getProvider() { return provider; }
    public String getFrequency() { return frequency; }
    public int getAvgPrice() { return avgPrice; }
    public String getDescription() { return description; }
}
