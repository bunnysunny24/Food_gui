import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List; // Import for GridLayout
import javax.swing.*; // Ensure this import is included

public class CollectorPortal {
    private OrderManagementSystem orderSystem;
    private List<Provider> providers; // Keep track of available providers
    private List<String> requests; // Keep track of requests made by collectors

    public CollectorPortal(OrderManagementSystem orderSystem) {
        this.orderSystem = orderSystem;
        this.providers = new ArrayList<>(); // Initialize the provider list
        this.requests = new ArrayList<>(); // Initialize requests list
    }

    public List<Provider> getProviders() {
        // Fetch providers from the database or return the existing list
        return providers; // Placeholder - replace with actual fetching logic
    }

    public boolean requestItem(String providerId, Collector collector) {
        // Logic to handle requesting items
        String request = "Request from " + collector.getName() + " to " + providerId;
        requests.add(request); // Store the request
        return true; // Assume the request was successful for simplicity
    }

    public List<String> getRequests() {
        // Return the list of requests made by the collector
        return requests;
    }

    public void displayCollectorMenu(Collector collector) {
        JFrame collectorFrame = new JFrame("Collector Menu");
        collectorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        collectorFrame.setSize(400, 400);
        collectorFrame.setLayout(new GridLayout(5, 1));

        JButton viewMyOrdersButton = new JButton("View My Orders");
        JButton requestItemsButton = new JButton("Request Items");
        JButton viewWastedItemsButton = new JButton("View Wasted Items");
        JButton addWastedItemButton = new JButton("Add Wasted Item");
        JButton logoutButton = new JButton("Logout");

        viewMyOrdersButton.addActionListener(e -> viewMyOrdersGUI(collector));
        requestItemsButton.addActionListener(e -> requestItemsGUI(collector));
        viewWastedItemsButton.addActionListener(e -> viewWastedItemsGUI());
        addWastedItemButton.addActionListener(e -> addWastedItemGUI(collector));
        logoutButton.addActionListener(e -> {
            collectorFrame.dispose();
            newfoodapp.showLoginForm(); // Back to login form
        });

        collectorFrame.add(viewMyOrdersButton);
        collectorFrame.add(requestItemsButton);
        collectorFrame.add(viewWastedItemsButton);
        collectorFrame.add(addWastedItemButton);
        collectorFrame.add(logoutButton);

        collectorFrame.setVisible(true);
    }

    private void viewMyOrdersGUI(Collector collector) {
        JFrame ordersFrame = new JFrame("My Orders");
        ordersFrame.setSize(400, 300);
        JTextArea ordersArea = new JTextArea();
        List<Order> orders = orderSystem.getOrdersForCollector(collector);

        for (Order order : orders) {
            ordersArea.append(order.toString() + "\n");
            // Show requested items
            for (FoodItem item : order.getRequestedItems()) {
                ordersArea.append("  - Requested Item: " + item.toString() + "\n");
            }
            // Show ordered items
            for (FoodItem item : order.getOrderedItems()) {
                ordersArea.append("  - Ordered Item: " + item.toString() + "\n");
            }
        }

        ordersFrame.add(new JScrollPane(ordersArea));
        ordersFrame.setVisible(true);
    }

    private void requestItemsGUI(Collector collector) {
        JFrame requestFrame = new JFrame("Request Food Items");
        requestFrame.setSize(400, 300);
        JTextArea requestArea = new JTextArea();

        // Fetch food items from the database and display them
        fetchAndDisplayFoodItems(requestArea);  // Pass the requestArea to display food items

        JTextField itemNameField = new JTextField("Enter item name to request");
        JButton sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(e -> {
            String itemName = itemNameField.getText();
            LocalDate dateWasted = LocalDate.now(); // Use current date
            FoodItem requestedItem = new FoodItem(itemName, 1, "unit", dateWasted); // Use actual quantity and unit
            Order order = new Order(1, /* provider */ null, collector, new Date(), new ArrayList<>(), new ArrayList<>());
            orderSystem.addOrder(order);
            orderSystem.addRequest(order, requestedItem);
            JOptionPane.showMessageDialog(requestFrame, "Request sent for " + itemName);
        });

        // Set layout to arrange components
        requestFrame.setLayout(new BorderLayout());
        requestFrame.add(new JScrollPane(requestArea), BorderLayout.CENTER);
        requestFrame.add(itemNameField, BorderLayout.NORTH);
        requestFrame.add(sendRequestButton, BorderLayout.SOUTH);
        requestFrame.setVisible(true);
    }

    private void fetchAndDisplayFoodItems(JTextArea requestArea) {
        List<FoodItem> foodItems = getFoodItemsForCollector(); // Fetch from database
        for (FoodItem item : foodItems) {
            // Display items in GUI
            requestArea.append(item.getName() + "\n"); // Update requestArea to show the food items
        }
    }

    private void viewWastedItemsGUI() {
        JFrame wastedItemsFrame = new JFrame("Wasted Items");
        wastedItemsFrame.setSize(400, 300);
        JTextArea wastedItemsArea = new JTextArea();
        List<FoodItem> wastedItems = getWastedItemsForCollector(); // Fetch wasted items

        for (FoodItem item : wastedItems) {
            wastedItemsArea.append(item.toString() + "\n");
        }

        wastedItemsFrame.add(new JScrollPane(wastedItemsArea));
        wastedItemsFrame.setVisible(true);
    }

    private void addWastedItemGUI(Collector collector) {
        JFrame addWastedItemFrame = new JFrame("Add Wasted Item");
        addWastedItemFrame.setSize(300, 200);
        JTextField wastedItemField = new JTextField("Enter wasted item name");
        JButton addButton = new JButton("Add Wasted Item");

        addButton.addActionListener(e -> {
            String itemName = wastedItemField.getText();
            // Logic to add wasted item to the database
            JOptionPane.showMessageDialog(addWastedItemFrame, "Wasted item '" + itemName + "' added.");
            addWastedItemFrame.dispose();
        });

        addWastedItemFrame.setLayout(new FlowLayout());
        addWastedItemFrame.add(wastedItemField);
        addWastedItemFrame.add(addButton);
        addWastedItemFrame.setVisible(true);
    }

    // Assuming this method exists to fetch food items from the database
    private List<FoodItem> getFoodItemsForCollector() {
        // Replace with actual database fetching logic
        // Example: return orderSystem.getFoodItems(); 
        return new ArrayList<>(); // Placeholder
    }

    // Placeholder method to fetch wasted items
    private List<FoodItem> getWastedItemsForCollector() {
        // Replace with actual logic to fetch wasted items from the database
        // Example: return orderSystem.getWastedItems(); 
        return new ArrayList<>(); // Placeholder
    }
}
