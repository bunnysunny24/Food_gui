import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; // Import BigDecimal
import javax.swing.*;

public class CollectorPortal {
    private OrderManagementSystem orderSystem;
    private List<Provider> providers;
    private List<String> requests;

    public CollectorPortal(OrderManagementSystem orderSystem, List<Provider> providers) {
        this.orderSystem = orderSystem;
        this.providers = providers;
        this.requests = new ArrayList<>();
    }

    public List<Provider> getProviders() {
        return providers; // Method to access providers
    }

    public boolean requestItem(String providerId, Collector collector) {
        String request = "Request from " + collector.getName() + " to " + providerId;
        requests.add(request);
        return true;
    }

    public void displayCollectorMenu(Collector collector) {
        JFrame collectorFrame = new JFrame("Collector Menu");
        collectorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        collectorFrame.setSize(400, 400);
        collectorFrame.setLayout(new GridLayout(4, 1));

        JButton checkWastedThingsButton = new JButton("Check Wasted Things");
        JButton requestItemsButton = new JButton("Request Items");
        JButton addWastedItemButton = new JButton("Add Wasted Item");
        JButton logoutButton = new JButton("Logout");

        checkWastedThingsButton.addActionListener(e -> viewWastedItemsGUI());
        requestItemsButton.addActionListener(e -> requestItemsGUI(collector));
        addWastedItemButton.addActionListener(e -> addWastedItemGUI(collector));
        logoutButton.addActionListener(e -> {
            collectorFrame.dispose();
            newfoodapp.showLoginForm();
        });

        collectorFrame.add(checkWastedThingsButton);
        collectorFrame.add(requestItemsButton);
        collectorFrame.add(addWastedItemButton);
        collectorFrame.add(logoutButton);

        collectorFrame.setVisible(true);
    }

    private void viewWastedItemsGUI() {
        // Create a new JFrame for displaying wasted items
        JFrame wastedItemsFrame = new JFrame("Wasted Items");
        wastedItemsFrame.setSize(400, 300);
        wastedItemsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close the frame properly
    
        // Create a JTextArea to display the wasted items
        JTextArea wastedItemsArea = new JTextArea();
        wastedItemsArea.setEditable(false); // Make it read-only
        wastedItemsArea.setLineWrap(true); // Enable line wrapping
        wastedItemsArea.setWrapStyleWord(true); // Wrap at word boundaries
    
        // Retrieve wasted items from the database
        List<WastedItem> wastedItems = getWastedItemsFromDatabase();
        for (WastedItem item : wastedItems) {
            wastedItemsArea.append(item.toString() + "\n"); // Append each item to the text area
        }
    
        // Add the JTextArea to a JScrollPane to allow scrolling
        JScrollPane scrollPane = new JScrollPane(wastedItemsArea);
        wastedItemsFrame.add(scrollPane); // Add the scroll pane to the frame
    
        // Center the frame on the screen
        wastedItemsFrame.setLocationRelativeTo(null);
        wastedItemsFrame.setVisible(true); // Make the frame visible
    }
    
    private List<WastedItem> getWastedItemsFromDatabase() {
        List<WastedItem> wastedItems = new ArrayList<>();
        String sql = "SELECT provider_username, food_item_name, quantity, unit, date_wasted FROM wasted_items"; // Updated SQL query to include unit
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String foodItemName = resultSet.getString("food_item_name");
                double quantity = resultSet.getDouble("quantity");
                String unit = resultSet.getString("unit"); // Fetch the unit
                LocalDate dateWasted = resultSet.getDate("date_wasted").toLocalDate();
                WastedItem item = new WastedItem(foodItemName, BigDecimal.valueOf(quantity), unit, dateWasted); // Updated constructor call

                wastedItems.add(item);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving wasted items: " + e.getMessage());
        }
        return wastedItems;
    }

    private void requestItemsGUI(Collector collector) {
        JFrame requestFrame = new JFrame("Request Food Items");
        requestFrame.setSize(400, 300);
        JTextArea requestArea = new JTextArea();
        fetchAndDisplayFoodItems(requestArea);

        JTextField itemNameField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField unitField = new JTextField();

        JButton sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(e -> {
            String itemName = itemNameField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String unit = unitField.getText().trim();

            if (itemName.isEmpty() || quantityStr.isEmpty() || unit.isEmpty()) {
                JOptionPane.showMessageDialog(requestFrame, "All fields must be filled.");
                return;
            }

            double quantity;
            try {
                quantity = Double.parseDouble(quantityStr);
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(requestFrame, "Invalid quantity. Please enter a positive number.");
                return;
            }

            LocalDate dateRequested = LocalDate.now();
            FoodItem requestedItem = new FoodItem(itemName, quantity, unit, dateRequested);
            Order order = new Order(1, null, collector, java.sql.Date.valueOf(dateRequested), new ArrayList<>(), new ArrayList<>());
            orderSystem.addOrder(order);
            orderSystem.addRequest(order, requestedItem);
            JOptionPane.showMessageDialog(requestFrame, "Request sent for " + itemName);
        });

        // Set layout to arrange components
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Unit:"));
        inputPanel.add(unitField);

        requestFrame.setLayout(new BorderLayout());
        requestFrame.add(new JScrollPane(requestArea), BorderLayout.CENTER);
        requestFrame.add(inputPanel, BorderLayout.NORTH);
        requestFrame.add(sendRequestButton, BorderLayout.SOUTH);
        requestFrame.setVisible(true);
    }

    private void fetchAndDisplayFoodItems(JTextArea requestArea) {
        List<FoodItem> foodItems = getFoodItemsForCollector();
        for (FoodItem item : foodItems) {
            requestArea.append(item.getName() + " - " + item.getQuantity() + " " + item.getUnit() + "\n");
        }
    }

    private List<FoodItem> getFoodItemsForCollector() {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT name, quantity, unit FROM food_items"; // Replace with your actual query
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double quantity = resultSet.getDouble("quantity");
                String unit = resultSet.getString("unit");
                foodItems.add(new FoodItem(name, quantity, unit, LocalDate.now())); // Assuming the date is current
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving food items: " + e.getMessage());
        }
        return foodItems;
    }

    private void addWastedItemGUI(Collector collector) {
        JFrame addWastedItemFrame = new JFrame("Add Wasted Item");
        addWastedItemFrame.setSize(300, 200);

        JTextField wastedItemField = new JTextField("Enter wasted item name");
        JTextField quantityField = new JTextField("Enter quantity");
        JTextField unitField = new JTextField("Enter unit");
        JButton addButton = new JButton("Add Wasted Item");

        addButton.addActionListener(e -> {
            String itemName = wastedItemField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            String unit = unitField.getText().trim();
            double quantity;

            try {
                quantity = Double.parseDouble(quantityStr);
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addWastedItemFrame, "Invalid quantity. Please enter a positive number.");
                return;
            }

            LocalDate dateWasted = LocalDate.now();
            WastedItem item = new WastedItem(itemName, BigDecimal.valueOf(quantity), unit, dateWasted); // Updated to use BigDecimal

            // Assuming the provider is selected; replace with actual provider selection logic
            if (!providers.isEmpty()) {
                Provider provider = providers.get(0); // Example, use actual selection
                provider.addWastedItem(item); // Changed to add the new WastedItem object
                JOptionPane.showMessageDialog(addWastedItemFrame, "Wasted item '" + itemName + "' added.");
            } else {
                JOptionPane.showMessageDialog(addWastedItemFrame, "No providers available to add the wasted item.");
            }
            addWastedItemFrame.dispose();
        });

        addWastedItemFrame.setLayout(new FlowLayout());
        addWastedItemFrame.add(wastedItemField);
        addWastedItemFrame.add(quantityField);
        addWastedItemFrame.add(unitField);
        addWastedItemFrame.add(addButton);
        addWastedItemFrame.setVisible(true);
    }
}
