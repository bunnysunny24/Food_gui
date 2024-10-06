import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        JFrame wastedItemsFrame = new JFrame("Wasted Items");
        wastedItemsFrame.setSize(400, 300);
        JTextArea wastedItemsArea = new JTextArea();
        wastedItemsArea.setEditable(false); // Make it read-only

        // Retrieve wasted items from the database
        List<WastedItem> wastedItems = getWastedItemsFromDatabase();
        for (WastedItem item : wastedItems) {
            wastedItemsArea.append(item.toString() + "\n");
        }

        wastedItemsFrame.add(new JScrollPane(wastedItemsArea));
        wastedItemsFrame.setVisible(true);
    }

    private List<WastedItem> getWastedItemsFromDatabase() {
        List<WastedItem> wastedItems = new ArrayList<>();
        String sql = "SELECT provider_username, food_item_name, quantity, date_wasted FROM wasted_items";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String providerUsername = resultSet.getString("provider_username");
                String foodItemName = resultSet.getString("food_item_name");
                double quantity = (double) resultSet.getInt("quantity"); // Changed to double
                LocalDate dateWasted = resultSet.getDate("date_wasted").toLocalDate();

                WastedItem item = new WastedItem(foodItemName, quantity, dateWasted); // Adjusted constructor
                wastedItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving wasted items: " + e.getMessage());
        }
        return wastedItems;
    }

    private void requestItemsGUI(Collector collector) {
        JFrame requestFrame = new JFrame("Request Food Items");
        requestFrame.setSize(400, 300);
        JTextArea requestArea = new JTextArea();
        fetchAndDisplayFoodItems(requestArea); // Pass the requestArea to display food items

        JTextField itemNameField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField unitField = new JTextField();

        JButton sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(e -> {
            String itemName = itemNameField.getText();
            String quantityStr = quantityField.getText();
            String unit = unitField.getText();

            if (itemName.trim().isEmpty() || quantityStr.trim().isEmpty() || unit.trim().isEmpty()) {
                JOptionPane.showMessageDialog(requestFrame, "All fields must be filled.");
                return;
            }

            double quantity;
            try {
                quantity = Double.parseDouble(quantityStr);
                if (quantity <= 0) {
                    throw new NumberFormatException(); // Handle non-positive values
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(requestFrame, "Invalid quantity. Please enter a positive number.");
                return;
            }

            LocalDate dateRequested = LocalDate.now(); // Use current date
            FoodItem requestedItem = new FoodItem(itemName, quantity, unit, dateRequested); // Create FoodItem
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
        List<FoodItem> foodItems = getFoodItemsForCollector(); // Fetch from database
        for (FoodItem item : foodItems) {
            // Display items in GUI
            requestArea.append(item.getName() + " - " + item.getQuantity() + " " + item.getUnit() + "\n"); // Update requestArea to show the food items
        }
    }

    // Assuming this method exists to fetch food items from the database
    private List<FoodItem> getFoodItemsForCollector() {
        // Replace with actual database fetching logic
        return new ArrayList<>(); // Placeholder
    }

    private void addWastedItemGUI(Collector collector) {
        JFrame addWastedItemFrame = new JFrame("Add Wasted Item");
        addWastedItemFrame.setSize(300, 200);
        
        JTextField wastedItemField = new JTextField("Enter wasted item name");
        JTextField quantityField = new JTextField("Enter quantity");
        JButton addButton = new JButton("Add Wasted Item");

        addButton.addActionListener(e -> {
            String itemName = wastedItemField.getText();
            String quantityStr = quantityField.getText();
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

            LocalDate date = LocalDate.now(); // Current date
            FoodItem foodItem = new FoodItem(itemName, quantity, "unit", date); // Create FoodItem

            // Assuming the provider is selected; replace with actual provider selection logic
            Provider provider = providers.get(0); // Just an example, use an actual selection mechanism

            provider.addWastedItem(new WastedItem(itemName, quantity, date)); // Call with WastedItem
            JOptionPane.showMessageDialog(addWastedItemFrame, "Wasted item '" + itemName + "' added.");
            addWastedItemFrame.dispose();
        });

        addWastedItemFrame.setLayout(new FlowLayout());
        addWastedItemFrame.add(wastedItemField);
        addWastedItemFrame.add(quantityField);
        addWastedItemFrame.add(addButton);
        addWastedItemFrame.setVisible(true);
    }
}
