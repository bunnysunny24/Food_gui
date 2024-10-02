import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Provider extends User {
    private String name;
    private List<FoodItem> wastedItems = new ArrayList<>();

    // Constructor for Provider
    public Provider(String username, String password, String role, String name) {
        super(username, password, role, name); // Correctly passing four arguments to super
        this.name = name; // Initialize the name
    }

    // Getter for name
    public String getName() {
        return this.name;
    }

    // Add a wasted item and save it to the database
    public void addWastedItem(FoodItem item) {
        this.wastedItems.add(item);
        saveWastedItemToDatabase(item); // Save to the database
    }

    // Getter for wasted items
    public List<FoodItem> getWastedItems() {
        return this.wastedItems;
    }

    // Method to save wasted item to the database
    public void saveWastedItemToDatabase(FoodItem item) {
        String sql = "INSERT INTO wasted_items (provider_username, food_item_name, quantity, date_wasted) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, getUsername()); // Assuming getUsername() returns the provider's username
            stmt.setString(2, item.getName()); // Assuming FoodItem has a getName() method
            stmt.setDouble(3, item.getQuantity()); // Use setDouble to match FoodItem's quantity type
            stmt.setDate(4, java.sql.Date.valueOf(item.getDateWasted())); // Assuming FoodItem has a getDateWasted() method
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to save wasted item: " + e.getMessage());
        }
    }
}
