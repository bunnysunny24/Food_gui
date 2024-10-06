import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;

public class ProviderPortal {
    private List<Provider> providers;

    public ProviderPortal(List<Provider> providers) {
        this.providers = providers;
    }

    // Method to display the wasted items for the provider
    public void displayWastedItemsGUI(Provider provider) {
        JFrame itemsFrame = new JFrame("Wasted Items");
        itemsFrame.setSize(400, 300);
        JTextArea itemsArea = new JTextArea();
        itemsArea.setEditable(false); // Make it read-only

        // Populate the JTextArea with the provider's wasted items from the database
        loadWastedItemsFromDatabase(provider, itemsArea);

        // Button to add a new wasted item
        JButton addItemButton = new JButton("Add Wasted Item");
        addItemButton.addActionListener(e -> {
            String itemName = JOptionPane.showInputDialog(itemsFrame, "Enter the name of the wasted item:");
            String quantityStr = JOptionPane.showInputDialog(itemsFrame, "Enter the quantity of the wasted item:");
            String unit = JOptionPane.showInputDialog(itemsFrame, "Enter the unit of the wasted item:");

            if (itemName != null && !itemName.trim().isEmpty() && quantityStr != null && unit != null) {
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        throw new NumberFormatException(); // To handle non-positive integers
                    }

                    // Create a new WastedItem and add it to the database
                    addWastedItemToDatabase(provider.getUsername(), itemName, quantity, unit);

                    // Update the displayed list of wasted items
                    itemsArea.append("Added wasted item: " + itemName + "\n");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(itemsFrame, "Invalid quantity. Please enter a positive number.");
                }
            } else {
                JOptionPane.showMessageDialog(itemsFrame, "Please enter valid details.");
            }
        });

        // Set up the layout
        itemsFrame.add(new JScrollPane(itemsArea), "Center");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addItemButton);
        itemsFrame.add(buttonPanel, "South");
        itemsFrame.setVisible(true);
    }

    private void loadWastedItemsFromDatabase(Provider provider, JTextArea itemsArea) {
        String sql = "SELECT food_item_name, quantity, date_wasted FROM wasted_items WHERE provider_username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, provider.getUsername());
            ResultSet resultSet = statement.executeQuery();

            // Populate the JTextArea with the data retrieved from the database
            while (resultSet.next()) {
                String foodItemName = resultSet.getString("food_item_name");
                int quantity = resultSet.getInt("quantity");
                LocalDate dateWasted = resultSet.getDate("date_wasted").toLocalDate();
                itemsArea.append(String.format("Item: %s, Quantity: %d, Date Wasted: %s%n", 
                    foodItemName, quantity, dateWasted));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading wasted items: " + e.getMessage());
        }
    }

    private void addWastedItemToDatabase(String providerUsername, String foodItemName, int quantity, String unit) {
        String sql = "INSERT INTO wasted_items (provider_username, food_item_name, quantity, date_wasted) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, providerUsername);
            statement.setString(2, foodItemName);
            statement.setInt(3, quantity);
            statement.setDate(4, java.sql.Date.valueOf(LocalDate.now())); // Set the current date
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving wasted item: " + e.getMessage());
        }
    }
}
