import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ProviderPortal {
    private List<Provider> providers;
    private List<String> requests;

    public ProviderPortal(List<Provider> providers) {
        this.providers = providers;
        this.requests = new ArrayList<>();
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void displayWastedItemsGUI(Provider provider) {
        JFrame itemsFrame = new JFrame("Wasted Items");
        itemsFrame.setSize(400, 300);
        JTextArea itemsArea = new JTextArea();
        
        // Populate wasted items
        for (FoodItem item : provider.getWastedItems()) {
            itemsArea.append(item.toString() + "\n");
        }

        // Button for sending requests
        JButton requestButton = new JButton("Send Request");
        requestButton.addActionListener(e -> {
            String selectedItemName = JOptionPane.showInputDialog(itemsFrame, "Enter The ID of Collector");
            if (selectedItemName != null && !selectedItemName.trim().isEmpty()) {
                requestItem(provider, selectedItemName);
            } else {
                JOptionPane.showMessageDialog(itemsFrame, "Please enter a valid Collector ID.");
            }
        });

        // Button to view past requests
        JButton viewRequestsButton = new JButton("View Past Requests");
        viewRequestsButton.addActionListener(e -> viewRequests(provider));

        // Setup layout
        itemsFrame.add(new JScrollPane(itemsArea), "Center");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(requestButton);
        buttonPanel.add(viewRequestsButton);
        itemsFrame.add(buttonPanel, "South");
        itemsFrame.setVisible(true);
        itemsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close
    }

    private void requestItem(Provider provider, String itemName) {
        // Here you may want to check if the item exists in provider's wasted items
        if (!requests.contains(itemName)) {
            requests.add(itemName);
            JOptionPane.showMessageDialog(null, "Request sent for " + itemName);
        } else {
            JOptionPane.showMessageDialog(null, "Request for " + itemName + " already sent.");
        }
    }

    private void viewRequests(Provider provider) {
        JFrame requestsFrame = new JFrame("Requests");
        requestsFrame.setSize(400, 300);
        JTextArea requestsArea = new JTextArea();

        for (String request : requests) {
            requestsArea.append(request + "\n");
        }

        requestsFrame.add(new JScrollPane(requestsArea), "Center");
        requestsFrame.setVisible(true);
        requestsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close
    }

    private void collectItems(String itemName) {
        JOptionPane.showMessageDialog(null, itemName + " has been collected!");
    }
}
