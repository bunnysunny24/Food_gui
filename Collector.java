import java.util.ArrayList;
import java.util.List;

public class Collector extends User {
    private List<FoodItem> collectedItems;

    public Collector(String username, String password, String name) {
        super(username, password, "collector", name); // Pass the name to the User constructor
        this.collectedItems = new ArrayList<>(); // Initialize the list
    }

    public void addCollectedItem(FoodItem item) {
        collectedItems.add(item); // Add item to the list
    }

    public List<FoodItem> getCollectedItems() {
        return collectedItems; // Get the list of collected items
    }
}
