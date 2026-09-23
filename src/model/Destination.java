package model;

/**
 * Represents an Indian travel destination.
 */
public class Destination {
    private String id;
    private String name;
    private String state;
    private String category;       // Heritage, Beach, Mountain, Spiritual, Metropolitan, Cultural
    private String description;
    private String image;
    private double latitude;
    private double longitude;
    private int avgDailyBudget;    // in INR (₹)
    private String bestTimeToVisit;
    private Attraction[] attractions;
    private Hotel[] hotels;
    private Restaurant[] restaurants;
    private Transport[] transports;

    public Destination(String id, String name, String state, String category, String description,
                       String image, double latitude, double longitude, int avgDailyBudget,
                       String bestTimeToVisit, Attraction[] attractions, Hotel[] hotels,
                       Restaurant[] restaurants, Transport[] transports) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.category = category;
        this.description = description;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avgDailyBudget = avgDailyBudget;
        this.bestTimeToVisit = bestTimeToVisit;
        this.attractions = attractions != null ? attractions : new Attraction[0];
        this.hotels = hotels != null ? hotels : new Hotel[0];
        this.restaurants = restaurants != null ? restaurants : new Restaurant[0];
        this.transports = transports != null ? transports : new Transport[0];
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getState() { return state; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getAvgDailyBudget() { return avgDailyBudget; }
    public String getBestTimeToVisit() { return bestTimeToVisit; }
    public Attraction[] getAttractions() { return attractions; }
    public Hotel[] getHotels() { return hotels; }
    public Restaurant[] getRestaurants() { return restaurants; }
    public Transport[] getTransports() { return transports; }
}
