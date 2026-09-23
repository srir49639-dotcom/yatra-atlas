package model;

/**
 * Represents hotel accommodation in a destination.
 */
public class Hotel {
    private String id;
    private String name;
    private String destinationId;
    private String tier;           // Luxury, Boutique, Mid-Range, Budget
    private int pricePerNight;     // in INR
    private double rating;
    private String address;
    private String[] amenities;

    public Hotel(String id, String name, String destinationId, String tier,
                 int pricePerNight, double rating, String address, String[] amenities) {
        this.id = id;
        this.name = name;
        this.destinationId = destinationId;
        this.tier = tier;
        this.pricePerNight = pricePerNight;
        this.rating = rating;
        this.address = address;
        this.amenities = amenities != null ? amenities : new String[0];
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDestinationId() { return destinationId; }
    public String getTier() { return tier; }
    public int getPricePerNight() { return pricePerNight; }
    public double getRating() { return rating; }
    public String getAddress() { return address; }
    public String[] getAmenities() { return amenities; }
}
