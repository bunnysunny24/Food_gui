import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class newfoodapp {
    private static UserAuthenticationSystem authSystem = new UserAuthenticationSystem();
    private static CollectorPortal collectorPortal;
    private static OrderManagementSystem orderSystem = new OrderManagementSystem();
    private static JFrame mainFrame;

    public static void main(String[] args) {
        // Initialize systems and sample data
        List<Provider> providers = new ArrayList<>();
        collectorPortal = new CollectorPortal(orderSystem, providers);
        initializeSampleData();
        showLoginForm();
    }

    public static void showLoginForm() {
        mainFrame = new JFrame("Food Waste Reduction System - Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLayout(new GridLayout(6, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Collector", "Provider"});

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> handleLogin(userField.getText().trim(), new String(passField.getPassword()), roleComboBox.getSelectedItem().toString().toLowerCase()));
        registerButton.addActionListener(e -> showRegistrationForm());

        // Add components to the frame
        mainFrame.add(userLabel);
        mainFrame.add(userField);
        mainFrame.add(passLabel);
        mainFrame.add(passField);
        mainFrame.add(roleLabel);
        mainFrame.add(roleComboBox);
        mainFrame.add(new JLabel("")); // Empty placeholder
        mainFrame.add(loginButton);
        mainFrame.add(registerButton);

        mainFrame.setVisible(true);
    }

    private static void handleLogin(String username, String password, String role) {
        User user = authSystem.authenticate(username, password, role);
        if (user != null) {
            mainFrame.dispose();
            if (user instanceof Collector) {
                showCollectorMenu((Collector) user);
            } else if (user instanceof Provider) {
                showProviderMenu((Provider) user);
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Login failed. Please try again.");
        }
    }

    private static void showRegistrationForm() {
        JFrame registrationFrame = new JFrame("Register User");
        registrationFrame.setSize(400, 300);
        registrationFrame.setLayout(new GridLayout(7, 2)); // Adjusted to 7 rows for new fields

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Collector", "Provider"});
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JButton registerButton = new JButton("Create Account");

        registerButton.addActionListener(e -> handleRegistration(userField.getText().trim(), new String(passField.getPassword()), roleComboBox.getSelectedItem().toString().toLowerCase(), nameField.getText().trim(), registrationFrame));

        // Add components to the frame
        registrationFrame.add(userLabel);
        registrationFrame.add(userField);
        registrationFrame.add(passLabel);
        registrationFrame.add(passField);
        registrationFrame.add(roleLabel);
        registrationFrame.add(roleComboBox);
        registrationFrame.add(nameLabel);
        registrationFrame.add(nameField);
        registrationFrame.add(new JLabel("")); // Empty placeholder
        registrationFrame.add(registerButton);

        registrationFrame.setVisible(true);
    }

    private static void handleRegistration(String username, String password, String role, String name, JFrame registrationFrame) {
        User newUser = null;
        if (role.equals("collector")) {
            newUser = new Collector(username, password, name);
        } else if (role.equals("provider")) {
            String providerId = JOptionPane.showInputDialog("Enter Provider ID:");
            newUser = new Provider(username, password, name, providerId);
        }

        if (newUser != null && authSystem.addUser(newUser)) {
            JOptionPane.showMessageDialog(registrationFrame, "Account created successfully!");
            registrationFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(registrationFrame, "Failed to create account. Username may already exist.");
        }
    }

    private static void showCollectorMenu(Collector collector) {
        JFrame collectorFrame = new JFrame("Collector Menu");
        collectorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        collectorFrame.setSize(400, 400);
        collectorFrame.setLayout(new GridLayout(5, 1)); // Change to 5 rows to accommodate the new button
    
        // Create buttons for the menu
        JButton viewProvidersButton = new JButton("View Nearby Providers");
        JButton sendRequestButton = new JButton("Send Food Request");
        JButton viewOrdersButton = new JButton("Check My Orders");
        JButton checkWastedItemsButton = new JButton("Check Wasted Items"); // New button
        JButton logoutButton = new JButton("Logout");
    
        // Action listeners for each button
        viewProvidersButton.addActionListener(e -> new Thread(() -> MapViewer.main(new String[]{})).start());
        sendRequestButton.addActionListener(e -> sendFoodRequestGUI(collector));
        viewOrdersButton.addActionListener(e -> viewCollectorOrdersGUI(collector));
    
        // Action for checking wasted items
        checkWastedItemsButton.addActionListener(e -> {
            ProviderPortal providerPortal = new ProviderPortal(); // Modify constructor if needed
            providerPortal.showAllWastedItemsPopup(); // Call the method to show all wasted items for all providers
        });
    
        // Logout button action
        logoutButton.addActionListener(e -> {
            collectorFrame.dispose(); // Close the collector frame
            showLoginForm(); // Show the login form again
        });
    
        // Add buttons to the frame
        collectorFrame.add(viewProvidersButton);
        collectorFrame.add(sendRequestButton);
        collectorFrame.add(viewOrdersButton);
        collectorFrame.add(checkWastedItemsButton); // Add the new button to the frame
        collectorFrame.add(logoutButton);
    
        collectorFrame.setVisible(true); // Make the frame visible
    }
    
    
    private static void showProviderMenu(Provider provider) {
        JFrame providerFrame = new JFrame("Provider Menu");
        providerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        providerFrame.setSize(400, 400);
        providerFrame.setLayout(new GridLayout(5, 1));
    
        JButton viewItemsButton = new JButton("View All Wasted Items");
        // Update the action listener to show all wasted items
        viewItemsButton.addActionListener(e -> {
            ProviderPortal portal = new ProviderPortal();
            portal.showAllWastedItemsPopup();
        });
    
        JButton addItemButton = new JButton("Add Wasted Item");
        addItemButton.addActionListener(e -> addWastedItemGUI(provider));
    
        JButton viewRequestsButton = new JButton("View Pending Requests");
        viewRequestsButton.addActionListener(e -> viewPendingRequestsGUI(provider));
    
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            providerFrame.dispose();
            showLoginForm();
        });
    
        // Add components to the frame
        providerFrame.add(viewItemsButton);
        providerFrame.add(addItemButton);
        providerFrame.add(viewRequestsButton);
        providerFrame.add(logoutButton);
    
        providerFrame.setVisible(true);
    }
    
    

    private static void sendFoodRequestGUI(Collector collector) {
        JFrame requestFrame = new JFrame("Send Food Request");
        requestFrame.setSize(400, 300);
        requestFrame.setLayout(new GridLayout(3, 1));

        JTextArea providersArea = new JTextArea();
        for (Provider provider : collectorPortal.getProviders()) {
            providersArea.append(provider.getName() + "\n");
        }

        JTextField providerIdField = new JTextField("Enter provider ID");
        JButton sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(e -> handleSendRequest(providerIdField.getText().trim(), collector, requestFrame));

        requestFrame.add(new JScrollPane(providersArea));
        requestFrame.add(providerIdField);
        requestFrame.add(sendRequestButton);

        requestFrame.setVisible(true);
    }

    private static void handleSendRequest(String providerId, Collector collector, JFrame requestFrame) {
        boolean success = collectorPortal.requestItem(providerId, collector);
        if (success) {
            JOptionPane.showMessageDialog(requestFrame, "Food request sent!");
        } else {
            JOptionPane.showMessageDialog(requestFrame, "Failed to send request. Check provider ID.");
        }
    }

    private static void viewCollectorOrdersGUI(Collector collector) {
        JFrame ordersFrame = new JFrame("My Orders");
        ordersFrame.setSize(400, 300);
        JTextArea ordersArea = new JTextArea();
        List<Order> orders = orderSystem.getOrdersForCollector(collector);

        for (Order order : orders) {
            ordersArea.append(order.toString() + "\n");
        }

        ordersFrame.add(new JScrollPane(ordersArea));
        ordersFrame.setVisible(true);
    }

    private static void addWastedItemGUI(Provider provider) {
        JFrame addItemFrame = new JFrame("Add Wasted Item");
        addItemFrame.setSize(400, 300);
        addItemFrame.setLayout(new GridLayout(5, 2));
        
        JLabel itemNameLabel = new JLabel("Item Name:");
        JTextField itemNameField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel unitLabel = new JLabel("Unit:");
        JTextField unitField = new JTextField(); // Added field for unit
        JButton addButton = new JButton("Add Wasted Item");
    
        addButton.addActionListener(e -> {
            try {
                String itemName = itemNameField.getText().trim();
                double quantity = Double.parseDouble(quantityField.getText().trim()); // Changed to double
                String unit = unitField.getText().trim(); // Get unit value
    
                // Create a WastedItem with BigDecimal for quantity
                BigDecimal quantityBigDecimal = BigDecimal.valueOf(quantity); // Convert double to BigDecimal
    
                // Create a WastedItem with LocalDate directly
                WastedItem item = new WastedItem(itemName, quantityBigDecimal, unit, LocalDate.now()); // Pass LocalDate directly
                provider.addWastedItem(item); // Add the WastedItem to the provider
                JOptionPane.showMessageDialog(addItemFrame, "Wasted item added!");
                addItemFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addItemFrame, "Invalid quantity. Please enter a valid number.");
            }
        });
    
        addItemFrame.add(itemNameLabel);
        addItemFrame.add(itemNameField);
        addItemFrame.add(quantityLabel);
        addItemFrame.add(quantityField);
        addItemFrame.add(unitLabel);
        addItemFrame.add(unitField); // Add the unit field to the frame
        addItemFrame.add(new JLabel("")); // Empty placeholder
        addItemFrame.add(addButton);
    
        addItemFrame.setVisible(true);
    }

    private static void viewPendingRequestsGUI(Provider provider) {
        JFrame requestsFrame = new JFrame("Pending Requests");
        requestsFrame.setSize(400, 300);
        JTextArea requestsArea = new JTextArea();
        List<Order> requests = orderSystem.getPendingRequestsForProvider(provider);

        for (Order request : requests) {
            requestsArea.append(request.toString() + "\n");
        }

        requestsFrame.add(new JScrollPane(requestsArea));
        requestsFrame.setVisible(true);
    }

    private static void initializeSampleData() {
        // Initialize some sample users and data for testing purposes
        Collector collector1 = new Collector("collector1", "pass", "John Doe");
        Collector collector2 = new Collector("collector2", "pass", "Jane Smith");
        Provider provider1 = new Provider("provider1", "pass", "Food Co.", "P001");
        Provider provider2 = new Provider("provider2", "pass", "Green Grocers", "P002");
        
        authSystem.addUser(collector1);
        authSystem.addUser(collector2);
        authSystem.addUser(provider1);
        authSystem.addUser(provider2);
    }
}
