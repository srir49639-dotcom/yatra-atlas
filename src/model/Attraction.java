package model;

/**
 * Represents a tourist attraction or landmark within a destination.
 */
public class Attraction {
    private String id;
    private String name;
    private String destinationId;
    private String category;
    private String description;
    private int entryFee;          // in INR
    private double rating;         // 1.0 to 5.0
    private String openingHours;
    private String image;

    public Attraction(String id, String name, String destinationId, String category,
                      String description, int entryFee, double rating, String openingHours, String image) {
        this.id = id;
        this.name = name;
        this.destinationId = destinationId;
        this.category = category;
        this.description = description;
        this.entryFee = entryFee;
        this.rating = rating;
        this.openingHours = openingHours;
        this.image = image;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDestinationId() { return destinationId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public int getEntryFee() { return entryFee; }
    public double getRating() { return rating; }
    public String getOpeningHours() { return openingHours; }
    public String getImage() { return image; }
}
