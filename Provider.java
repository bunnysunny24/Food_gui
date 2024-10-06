import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Provider extends User {
    private String name;
    private List<FoodItem> wastedItems = new ArrayList<>();

    // Constructor
    public Provider(String username, String password, String role, String name) {
        super(username, password, role, name);
        this.name = name;
    }

    // Getter for the name
    public String getName() {
        return this.name;
    }

    // Method to add a wasted item
    public void addWastedItem(FoodItem item) {
        this.wastedItems.add(item);
        this.saveWastedItemToDatabase(item); // Save to database
    }

    // Getter for wasted items
    public List<FoodItem> getWastedItems() {
        return this.wastedItems;
    }

    // Method to save wasted item to the database
    public void saveWastedItemToDatabase(FoodItem item) {
        String sql = "INSERT INTO wasted_items (provider_username, food_item_name, quantity, date_wasted) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); 
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL statement
            statement.setString(1, this.getUsername());
            statement.setString(2, item.getName());
            statement.setDouble(3, item.getQuantity());
            statement.setDate(4, Date.valueOf(item.getDateWasted()));
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to save wasted item: " + e.getMessage());
        }
    }
}
