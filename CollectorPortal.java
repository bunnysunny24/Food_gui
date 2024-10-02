import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CollectorPortal {
    private static List<Collector> collectors = new ArrayList<>();
    private static List<Provider> providers = new ArrayList<>();

    // Getters for collectors and providers
    public static List<Collector> getCollectors() {
        return collectors;
    }

    public static List<Provider> getProviders() {
        return providers;
    }

    // Method to add a collector
    public void addCollector(Collector collector) {
        collectors.add(collector);
    }

    // Method to add a provider
    public void addProvider(Provider provider) {
        providers.add(provider);
    }

    // Request item method
    public boolean requestItem(Provider provider, String itemName) {
        for (FoodItem item : provider.getWastedItems()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                JOptionPane.showMessageDialog(null, "Request for " + itemName + " sent to " + provider.getName());
                return true; // Successful request
            }
        }
        return false; // Item not found
    }
}
