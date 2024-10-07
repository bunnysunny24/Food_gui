import java.math.BigDecimal; // Use BigDecimal for quantity
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Provider extends User {
    private List<WastedItem> wastedItems; // List of wasted food items
    private String providerId; // Field for provider ID

    // Constructor
    public Provider(String username, String password, String name, String providerId) {
        super(username, password, "Provider", name);
        this.providerId = providerId;
        this.wastedItems = new ArrayList<>();
    }

    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public boolean addWastedItem(WastedItem item) {
        if (!isValidWastedItem(item)) {
            return false;
        }

        wastedItems.add(item); // Add to the local list

        // Save the wasted item to the database
        if (saveWastedItemToDatabase(item)) {
            System.out.println("Wasted item '" + item.getName() + "' added successfully.");
            return true;
        } else {
            System.out.println("Failed to add wasted item '" + item.getName() + "' to the database.");
            return false;
        }
    }

    private boolean isValidWastedItem(WastedItem item) {
        if (item == null) {
            System.out.println("Cannot add wasted item: Item is null.");
            return false;
        }
        if (item.getName() == null || item.getDateWasted() == null) {
            System.out.println("Cannot add wasted item: Name or date is null.");
            return false;
        }
        // Check quantity for BigDecimal
        if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Cannot add wasted item: Quantity must be a positive number.");
            return false;
        }
        return true;
    }

    public List<WastedItem> getWastedItems() {
        return new ArrayList<>(wastedItems); // Return a copy of the list
    }

    private boolean saveWastedItemToDatabase(WastedItem item) {
        String sql = "INSERT INTO wasted_items (provider_username, food_item_name, quantity, unit, date_wasted) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, getUsername());
            statement.setString(2, item.getName());
            statement.setBigDecimal(3, item.getQuantity()); // Change to BigDecimal
            statement.setString(4, item.getUnit());
            statement.setDate(5, Date.valueOf(item.getDateWasted())); // Convert LocalDate to SQL Date

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error: Failed to save wasted item '" + item.getName() + "' - " + e.getMessage());
            return false;
        }
    }

    // Method to retrieve wasted items from the database
    public void loadWastedItemsFromDatabase() {
        String sql = "SELECT food_item_name, quantity, unit, date_wasted FROM wasted_items WHERE provider_username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, getUsername());
            ResultSet resultSet = statement.executeQuery();

            wastedItems.clear(); // Clear the existing list before loading
            while (resultSet.next()) {
                String name = resultSet.getString("food_item_name");
                BigDecimal quantity = resultSet.getBigDecimal("quantity"); // Use BigDecimal
                String unit = resultSet.getString("unit");
                LocalDate dateWasted = resultSet.getDate("date_wasted").toLocalDate();

                WastedItem item = new WastedItem(name, quantity, unit, dateWasted);
                wastedItems.add(item);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: Failed to load wasted items - " + e.getMessage());
        }
    }

    public List<Request> getPendingRequests() {
        return new ArrayList<>(); // Placeholder implementation
    }
}
