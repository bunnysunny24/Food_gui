import java.util.ArrayList;
import java.util.List;

public class OrderManagementSystem {
    private List<Order> orders = new ArrayList<>(); // Keep track of orders

    public List<Order> getOrdersForCollector(Collector collector) {
        List<Order> collectorOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCollector().equals(collector)) {
                collectorOrders.add(order);
            }
        }
        return collectorOrders; // Return orders for the specified collector
    }

    public List<Order> getPendingOrdersForProvider(Provider provider) {
        List<Order> providerOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getProvider().equals(provider)) {
                providerOrders.add(order);
            }
        }
        return providerOrders; // Return pending orders for the specified provider
    }

    // Method to add an order
    public void addOrder(Order order) {
        orders.add(order);
    }
}
