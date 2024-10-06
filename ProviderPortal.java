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

        for (FoodItem item : provider.getWastedItems()) {
            itemsArea.append(item.toString() + "\n");
        }

        JButton requestButton = new JButton("Send Request");
        requestButton.addActionListener(e -> {
            String selectedItemName = JOptionPane.showInputDialog(itemsFrame, "Enter The ID of Collector");
            requestItem(provider, selectedItemName);
        });

        JButton viewRequestsButton = new JButton("View Past Requests");
        viewRequestsButton.addActionListener(e -> viewRequests(provider));

        itemsFrame.add(new JScrollPane(itemsArea), "Center");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(requestButton);
        buttonPanel.add(viewRequestsButton);
        itemsFrame.add(buttonPanel, "South");
        itemsFrame.setVisible(true);
    }

    private void requestItem(Provider provider, String itemName) {
        requests.add(itemName);
        JOptionPane.showMessageDialog(null, "Request sent for " + itemName);
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
    }

    private void collectItems(String itemName) {
        JOptionPane.showMessageDialog(null, itemName + " has been collected!");
    }
}
