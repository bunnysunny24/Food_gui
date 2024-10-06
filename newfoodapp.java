import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList; // Import ArrayList
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
        List<Provider> providers = new ArrayList<>(); // Initialize your list of providers
        collectorPortal = new CollectorPortal(orderSystem, providers); // Updated constructor call
        initializeSampleData();
        showLoginForm();
    }

    public static void showLoginForm() {
        mainFrame = new JFrame("Food Waste Reduction System - Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLayout(new GridLayout(6, 2));

        // Username field
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        // Password field
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        // Role selection field (changed to JComboBox)
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Collector", "Provider"}; // Options for the dropdown
        JComboBox<String> roleComboBox = new JComboBox<>(roles); // Create JComboBox

        // Login button
        JButton loginButton = new JButton("Login");

        // Register button
        JButton registerButton = new JButton("Register");

        // Login button action
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String role = roleComboBox.getSelectedItem().toString().toLowerCase(); // Get selected role
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
        });

        // Register button action
        registerButton.addActionListener(e -> showRegistrationForm());

        mainFrame.add(userLabel);
        mainFrame.add(userField);
        mainFrame.add(passLabel);
        mainFrame.add(passField);
        mainFrame.add(roleLabel);
        mainFrame.add(roleComboBox); // Add the JComboBox for role selection
        mainFrame.add(new JLabel("")); // Empty placeholder
        mainFrame.add(loginButton);
        mainFrame.add(registerButton); // Add the register button

        mainFrame.setVisible(true);
    }

    private static void showRegistrationForm() {
        JFrame registrationFrame = new JFrame("Register User");
        registrationFrame.setSize(400, 300);
        registrationFrame.setLayout(new GridLayout(6, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role:"); // Change to just "Role:"
        String[] roles = {"Collector", "Provider"}; // Options for the dropdown
        JComboBox<String> roleComboBox = new JComboBox<>(roles); // Create JComboBox for roles
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JButton registerButton = new JButton("Create Account");

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String role = roleComboBox.getSelectedItem().toString().toLowerCase(); // Get selected role
            String name = nameField.getText();

            // Create user based on role
            User newUser = null;
            if (role.equals("collector")) {
                newUser = new Collector(username, password, name);
            } else if (role.equals("provider")) {
                String providerId = JOptionPane.showInputDialog("Enter Provider ID:"); // Prompt for provider ID
                newUser = new Provider(username, password, name, providerId);
            }

            if (newUser != null && authSystem.addUser(newUser)) {
                JOptionPane.showMessageDialog(registrationFrame, "Account created successfully!");
                registrationFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(registrationFrame, "Failed to create account. Username may already exist.");
            }
        });

        registrationFrame.add(userLabel);
        registrationFrame.add(userField);
        registrationFrame.add(passLabel);
        registrationFrame.add(passField);
        registrationFrame.add(roleLabel);
        registrationFrame.add(roleComboBox); // Add JComboBox for role selection
        registrationFrame.add(nameLabel);
        registrationFrame.add(nameField);
        registrationFrame.add(new JLabel("")); // Empty placeholder
        registrationFrame.add(registerButton);

        registrationFrame.setVisible(true);
    }

    private static void showCollectorMenu(Collector collector) {
        JFrame collectorFrame = new JFrame("Collector Menu");
        collectorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        collectorFrame.setSize(400, 400);
        collectorFrame.setLayout(new GridLayout(4, 1));

        JButton viewProvidersButton = new JButton("View Nearby Providers");
        JButton sendRequestButton = new JButton("Send Food Request");
        JButton viewOrdersButton = new JButton("Check Wasted Things"); // Renamed
        JButton logoutButton = new JButton("Logout");

        viewProvidersButton.addActionListener(e -> new Thread(() -> MapViewer.main(new String[]{})).start());
        sendRequestButton.addActionListener(e -> sendFoodRequestGUI(collector));
        viewOrdersButton.addActionListener(e -> viewCollectorOrdersGUI(collector));
        logoutButton.addActionListener(e -> {
            collectorFrame.dispose();
            showLoginForm();
        });

        collectorFrame.add(viewProvidersButton);
        collectorFrame.add(sendRequestButton);
        collectorFrame.add(viewOrdersButton);
        collectorFrame.add(logoutButton);

        collectorFrame.setVisible(true);
    }

    private static void showProviderMenu(Provider provider) {
        JFrame providerFrame = new JFrame("Provider Menu");
        providerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        providerFrame.setSize(400, 400);
        providerFrame.setLayout(new GridLayout(5, 1));

        JButton viewItemsButton = new JButton("View Wasted Items");
        viewItemsButton.addActionListener(e -> {
            ProviderPortal providerPortal = new ProviderPortal(List.of(provider));
            providerPortal.displayWastedItemsGUI(provider);
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
        for (Provider provider : collectorPortal.getProviders()) { // Should work now
            providersArea.append(provider.getName() + "\n");
        }

        JTextField providerIdField = new JTextField("Enter provider ID");

        JButton sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(e -> {
            String providerId = providerIdField.getText();
            boolean success = collectorPortal.requestItem(providerId, collector); // Should work now
            if (success) {
                JOptionPane.showMessageDialog(requestFrame, "Food request sent!");
            } else {
                JOptionPane.showMessageDialog(requestFrame, "Failed to send request. Check provider ID.");
            }
        });

        requestFrame.add(new JScrollPane(providersArea));
        requestFrame.add(providerIdField);
        requestFrame.add(sendRequestButton);

        requestFrame.setVisible(true);
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
        addItemFrame.setLayout(new GridLayout(4, 2));

        JLabel itemNameLabel = new JLabel("Item Name:");
        JTextField itemNameField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel dateLabel = new JLabel("Date Wasted (YYYY-MM-DD):");
        JTextField dateField = new JTextField();

        JButton addButton = new JButton("Add Wasted Item");
        addButton.addActionListener(e -> {
            String itemName = itemNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            LocalDate dateWasted = LocalDate.parse(dateField.getText()); // Use LocalDate
            WastedItem wastedItem = new WastedItem(itemName, quantity, dateWasted);
            provider.addWastedItem(wastedItem);
            JOptionPane.showMessageDialog(addItemFrame, "Wasted item added successfully!");
            addItemFrame.dispose();
        });

        addItemFrame.add(itemNameLabel);
        addItemFrame.add(itemNameField);
        addItemFrame.add(quantityLabel);
        addItemFrame.add(quantityField);
        addItemFrame.add(dateLabel);
        addItemFrame.add(dateField);
        addItemFrame.add(new JLabel()); // Empty placeholder
        addItemFrame.add(addButton);

        addItemFrame.setVisible(true);
    }

    private static void viewPendingRequestsGUI(Provider provider) {
        JFrame requestsFrame = new JFrame("Pending Requests");
        requestsFrame.setSize(400, 300);
        JTextArea requestsArea = new JTextArea();

        List<Order> pendingRequests = orderSystem.getPendingRequestsForProvider(provider);
        for (Order request : pendingRequests) {
            requestsArea.append(request.toString() + "\n");
        }

        requestsFrame.add(new JScrollPane(requestsArea));
        requestsFrame.setVisible(true);
    }

    private static void initializeSampleData() {
        // Create sample data for testing purposes
        Collector collector = new Collector("collector1", "password1", "Alice");
        Provider provider = new Provider("provider1", "password2", "Provider One", "P001");
        authSystem.addUser(collector);
        authSystem.addUser(provider);
    }
}
