import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private Provider provider;
    private Collector collector;
    private Date orderDate;
    private List<FoodItem> orderedItems; // Add a list of ordered items

    public Order(int orderId, Provider provider, Collector collector, Date orderDate, List<FoodItem> orderedItems) {
        this.orderId = orderId;
        this.provider = provider;
        this.collector = collector;
        this.orderDate = orderDate;
        this.orderedItems = orderedItems;
    }

    public Provider getProvider() {
        return provider; // Add getter method
    }

    public Collector getCollector() {
        return collector; // Add getter method
    }

    public Date getOrderDate() {
        return orderDate; // Add getter method
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Provider: " + provider.getName() + ", Collector: " + collector.getUsername() + ", Date: " + orderDate;
    }
}
