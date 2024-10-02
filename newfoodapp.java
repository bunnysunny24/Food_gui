import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate; // Import for LocalDate
import java.util.List;
import javax.swing.JButton;
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
        collectorPortal = new CollectorPortal(orderSystem);
        initializeSampleData();
        showLoginForm();
    }

    public static void showLoginForm() {
        mainFrame = new JFrame("Food Waste Reduction System - Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLayout(new GridLayout(5, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel roleLabel = new JLabel("Role (collector/provider):");
        JTextField roleField = new JTextField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                String role = roleField.getText().toLowerCase();
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
        });

        mainFrame.add(userLabel);
        mainFrame.add(userField);
        mainFrame.add(passLabel);
        mainFrame.add(passField);
        mainFrame.add(roleLabel);
        mainFrame.add(roleField);
        mainFrame.add(new JLabel("")); // Empty placeholder
        mainFrame.add(loginButton);

        mainFrame.setVisible(true);
    }

    private static void showCollectorMenu(Collector collector) {
        JFrame collectorFrame = new JFrame("Collector Menu");
        collectorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        collectorFrame.setSize(400, 400);
        collectorFrame.setLayout(new GridLayout(4, 1));

        JButton viewProvidersButton = new JButton("View Nearby Providers");
        JButton sendRequestButton = new JButton("Send Food Request");
        JButton viewOrdersButton = new JButton("View My Orders");
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

        JLabel nameLabel = new JLabel("Item Name:");
        JTextField nameField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel unitLabel = new JLabel("Unit:");
        JTextField unitField = new JTextField();
        JButton addItemButton = new JButton("Add Item");

        addItemButton.addActionListener(e -> {
            String name = nameField.getText();
            double quantity;
            try {
                quantity = Double.parseDouble(quantityField.getText());
                String unit = unitField.getText();
                
                // Set the date wasted to the current date
                LocalDate dateWasted = LocalDate.now(); // Current date
                FoodItem item = new FoodItem(name, quantity, unit, dateWasted); // Create FoodItem with date wasted
                provider.addWastedItem(item);
                JOptionPane.showMessageDialog(addItemFrame, "Item added!");
                addItemFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addItemFrame, "Invalid quantity. Please enter a number.");
            }
        });

        addItemFrame.add(nameLabel);
        addItemFrame.add(nameField);
        addItemFrame.add(quantityLabel);
        addItemFrame.add(quantityField);
        addItemFrame.add(unitLabel);
        addItemFrame.add(unitField);
        addItemFrame.add(new JLabel("")); // Placeholder
        addItemFrame.add(addItemButton);

        addItemFrame.setVisible(true);
    }

    private static void viewPendingRequestsGUI(Provider provider) {
        JFrame requestsFrame = new JFrame("Pending Requests");
        requestsFrame.setSize(400, 300);
        JTextArea requestsArea = new JTextArea();

        List<String> requests = collectorPortal.getRequests(); // Should work now

        for (String request : requests) {
            requestsArea.append(request + "\n");
        }

        requestsFrame.add(new JScrollPane(requestsArea));
        requestsFrame.setVisible(true);
    }

    private static void initializeSampleData() {
        // Sample data initialization
        Collector collector = new Collector("collector1", "pass123", "John Doe");
        Provider provider = new Provider("provider1", "pass456", "provider", "henry"); // Corrected provider creation
        authSystem.addUser(collector);
        authSystem.addUser(provider);
    }
}
