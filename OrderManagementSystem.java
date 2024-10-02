import java.util.ArrayList;
import java.util.List;

public class OrderManagementSystem {
    private List<Order> orders = new ArrayList<>();

    // Method to get orders for a specific collector
    public List<Order> getOrdersForCollector(Collector collector) {
        List<Order> collectorOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCollector().equals(collector)) {
                collectorOrders.add(order);
            }
        }
        return collectorOrders;
    }

    // Method to get pending orders for a specific provider
    public List<Order> getPendingOrdersForProvider(Provider provider) {
        List<Order> providerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getProvider().equals(provider)) {
                providerOrders.add(order);
            }
        }
        return providerOrders;
    }

    // Method to add a new order
    public void addOrder(Order order) {
        orders.add(order);
    }

    // New method to add a request to an existing order
    public void addRequest(Order order, FoodItem item) {
        order.getRequestedItems().add(item);
    }

    // New method to send a request for food items (could be used to add requests)
    public void sendRequest(Collector collector, FoodItem item) {
        // This is a placeholder for where you would implement your logic to handle requests.
        // Typically, you would fetch the order based on the collector and add the item to that order.
        for (Order order : orders) {
            if (order.getCollector().equals(collector)) {
                addRequest(order, item);
                System.out.println("Request for " + item.getName() + " sent successfully.");
                return;
            }
        }
        System.out.println("No orders found for collector " + collector.getUsername());
    }

    // Additional methods can be added as needed for managing orders and requests
}
