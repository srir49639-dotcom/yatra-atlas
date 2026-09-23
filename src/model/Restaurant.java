package model;

/**
 * Represents a culinary restaurant in a destination.
 */
public class Restaurant {
    private String id;
    private String name;
    private String destinationId;
    private String cuisine;
    private int avgCostForTwo;     // in INR
    private double rating;
    private String famousDish;
    private String address;

    public Restaurant(String id, String name, String destinationId, String cuisine,
                      int avgCostForTwo, double rating, String famousDish, String address) {
        this.id = id;
        this.name = name;
        this.destinationId = destinationId;
        this.cuisine = cuisine;
        this.avgCostForTwo = avgCostForTwo;
        this.rating = rating;
        this.famousDish = famousDish;
        this.address = address;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDestinationId() { return destinationId; }
    public String getCuisine() { return cuisine; }
    public int getAvgCostForTwo() { return avgCostForTwo; }
    public double getRating() { return rating; }
    public String getFamousDish() { return famousDish; }
    public String getAddress() { return address; }
}
