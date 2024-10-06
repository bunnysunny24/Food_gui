import java.util.ArrayList;
import java.util.List;

public class Collector extends User {
    private List<FoodItem> collectedItems; // List to store collected food items

    // Constructor that initializes the collector
    public Collector(String username, String password, String name) {
        super(username, password, "Collector", name); // Call the User constructor
        this.collectedItems = new ArrayList<>(); // Initialize the list of collected items
    }

    // Method to add a collected item
    public void addCollectedItem(FoodItem item) {
        if (item != null) {
            collectedItems.add(item); // Add the item to the collected items list
        }
    }

    // Method to get the list of collected items
    public List<FoodItem> getCollectedItems() {
        return collectedItems; // Return the list of collected items
    }
}
