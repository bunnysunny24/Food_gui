import java.util.ArrayList;
import java.util.List;

public class Provider extends User {
    private String name;
    private List<FoodItem> wastedItems;

    public Provider(String username, String password, String name) {
        super(username, password, "provider");
        this.name = name;
        this.wastedItems = new ArrayList<>(); // Initialize the list
    }

    public String getName() {
        return this.name;
    }

    public void addWastedItem(FoodItem item) {
        wastedItems.add(item); // Add item to the list
    }

    public List<FoodItem> getWastedItems() {
        return wastedItems; // Get the list of wasted items
    }
}
