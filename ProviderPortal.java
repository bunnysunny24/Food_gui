import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;

public class ProviderPortal {
    // SQL query constants
    private static final String SELECT_ALL_WASTED_ITEMS = "SELECT food_item_name, quantity, date_wasted, provider_username FROM wasted_items";
    private static final String INSERT_WASTED_ITEM = "INSERT INTO wasted_items (provider_username, food_item_name, quantity, unit, date_wasted) VALUES (?, ?, ?, ?, ?)";

    // No need for List<Provider> in the constructor for showing all wasted items
    public ProviderPortal() {
        // Optional: initialize if needed
    }

    // Method to show all wasted items for all providers
    public void showAllWastedItemsPopup() {
        JDialog orderDialog = new JDialog();
        orderDialog.setTitle("All Wasted Items");
        orderDialog.setSize(400, 300);
        JTextArea itemsArea = new JTextArea();
        itemsArea.setEditable(false); // Make it read-only

        // Load all wasted items from the database
        loadAllWastedItemsFromDatabase(itemsArea);

        // Set up the layout for the dialog
        orderDialog.setLayout(new BorderLayout());
        orderDialog.add(new JScrollPane(itemsArea), BorderLayout.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> orderDialog.dispose());
        orderDialog.add(closeButton, BorderLayout.SOUTH);
        orderDialog.setVisible(true);
    }

    // Load all wasted items from the database for all providers
    private void loadAllWastedItemsFromDatabase(JTextArea itemsArea) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_WASTED_ITEMS)) {

            ResultSet resultSet = statement.executeQuery();

            // Clear the JTextArea before loading items
            itemsArea.setText("");

            // Populate the JTextArea with the data retrieved from the database
            while (resultSet.next()) {
                String foodItemName = resultSet.getString("food_item_name");
                int quantity = resultSet.getInt("quantity");
                LocalDate dateWasted = resultSet.getDate("date_wasted").toLocalDate();
                String providerUsername = resultSet.getString("provider_username");
                itemsArea.append(String.format("Provider: %s, Item: %s, Quantity: %d, Date Wasted: %s%n",
                        providerUsername, foodItemName, quantity, dateWasted));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Error loading wasted items: " + e.getMessage());
        }
    }

    // Method to show wasted items for a specific provider (not necessary for the current context)
    public void showWastedItemsForProvider(Provider provider) {
        // This method is not currently needed since we are focusing on showing all items.
    }

    private void addWastedItemDialog(Provider provider, JTextArea itemsArea) {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField itemNameField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField unitField = new JTextField();

        panel.add(new JLabel("Item Name:"));
        panel.add(itemNameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Unit:"));
        panel.add(unitField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Wasted Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String itemName = itemNameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String unit = unitField.getText().trim();

            if (itemName.isEmpty() || quantityStr.isEmpty() || unit.isEmpty()) {
                showErrorDialog("All fields must be filled out.");
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    throw new NumberFormatException(); // To handle non-positive integers
                }

                // Create a new WastedItem and add it to the database
                addWastedItemToDatabase(provider.getUsername(), itemName, quantity, unit);

                // Reload wasted items to reflect the changes
                loadAllWastedItemsFromDatabase(itemsArea); 

                // Optionally, show a confirmation message
                JOptionPane.showMessageDialog(null, String.format("Wasted item added: %s, Quantity: %d %s, Date Wasted: %s",
                        itemName, quantity, unit, LocalDate.now()));

            } catch (NumberFormatException ex) {
                showErrorDialog("Invalid quantity. Please enter a positive integer.");
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorDialog("Error saving wasted item: " + e.getMessage());
            }
        }
    }

    private void addWastedItemToDatabase(String providerUsername, String foodItemName, int quantity, String unit) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_WASTED_ITEM)) {

            statement.setString(1, providerUsername);
            statement.setString(2, foodItemName);
            statement.setInt(3, quantity);
            statement.setString(4, unit);
            statement.setDate(5, java.sql.Date.valueOf(LocalDate.now())); // Set the current date
            statement.executeUpdate();
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
