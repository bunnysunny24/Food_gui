import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private Provider provider;
    private Collector collector;
    private Date orderDate;
    private List<FoodItem> orderedItems; // Items that were ordered
    private List<FoodItem> requestedItems; // Items that were requested
    private String status; // New field for order status

    // Constructor
    public Order(int orderId, Provider provider, Collector collector, Date orderDate, List<FoodItem> orderedItems, List<FoodItem> requestedItems) {
        this.orderId = orderId;
        this.provider = provider;
        this.collector = collector;
        this.orderDate = orderDate;
        this.orderedItems = orderedItems;
        this.requestedItems = requestedItems;
        this.status = "Pending"; // Default status
    }

    // Getters
    public Provider getProvider() {
        return provider;
    }

    public Collector getCollector() {
        return collector;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public List<FoodItem> getOrderedItems() {
        return orderedItems; // Return ordered items
    }

    public List<FoodItem> getRequestedItems() {
        return requestedItems; // Return requested items
    }

    public String getStatus() {
        return status; // Getter for status
    }

    public void setStatus(String status) {
        this.status = status; // Setter for status (optional, if you need to update it)
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
               ", Provider: " + provider.getName() +
               ", Collector: " + collector.getUsername() +
               ", Date: " + orderDate +
               ", Status: " + status; // Include status in the string representation
    }
}
