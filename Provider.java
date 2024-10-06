import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Provider extends User {
    private List<FoodItem> wastedItems = new ArrayList<>(); // List of wasted food items
    private String providerId; // Field for provider ID

    // Constructor
    public Provider(String username, String password, String name, String providerId) {
        super(username, password, "Provider", name); // Set role to "Provider" directly
        this.providerId = providerId; // Initialize provider ID
    }

    // Getter for the provider ID
    public String getProviderId() {
        return providerId; // Return provider ID
    }

    // Getter for the provider's name (overrides parent's getName)
    @Override
    public String getName() {
        return super.getName(); // Use parent's getName method
    }

    // Method to add a wasted item
    public void addWastedItem(FoodItem item) {
        if (item == null || item.getName() == null || item.getDateWasted() == null) {
            System.out.println("Cannot add wasted item: Invalid item data.");
            return; // Exit if the item data is invalid
        }

        this.wastedItems.add(item); // Add the item to the list
        if (this.saveWastedItemToDatabase(item)) {
            System.out.println("Wasted item '" + item.getName() + "' added successfully.");
        } else {
            System.out.println("Failed to add wasted item '" + item.getName() + "' to the database.");
        }
    }

    // Getter for wasted items
    public List<FoodItem> getWastedItems() {
        return new ArrayList<>(this.wastedItems); // Return a copy to prevent modification
    }

    // Method to save wasted item to the database
    private boolean saveWastedItemToDatabase(FoodItem item) {
        String sql = "INSERT INTO wasted_items (provider_username, food_item_name, quantity, date_wasted) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL statement
            statement.setString(1, this.getUsername()); // Use the provider's username
            statement.setString(2, item.getName());
            statement.setDouble(3, item.getQuantity());
            statement.setDate(4, Date.valueOf(item.getDateWasted())); // Convert LocalDate to SQL Date

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            System.out.println("SQL Error: Failed to save wasted item '" + item.getName() + "' - " + e.getMessage());
        }

        return false; // Return false if insertion fails
    }
}
