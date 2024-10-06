import java.util.ArrayList;
import java.util.List;

public class Collector extends User {
    private List<FoodItem> collectedItems;

    public Collector(String username, String password, String name) {
        super(username, password, "collector", name); 
        this.collectedItems = new ArrayList<>(); 
    }

    public void addCollectedItem(FoodItem item) {
        collectedItems.add(item); 
    }

    public List<FoodItem> getCollectedItems() {
        return collectedItems; 
    }
}
