public class Request {
    private int requestId;
    private String collectorName;
    private String itemName;
    private int quantity;
    private java.util.Date requestDate;
    private String status;

    // Constructor
    public Request(int requestId, String collectorName, String itemName, int quantity, java.util.Date requestDate, String status) {
        this.requestId = requestId;
        this.collectorName = collectorName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.requestDate = requestDate;
        this.status = status;
    }

    // Getters
    public int getRequestId() {
        return requestId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public java.util.Date getRequestDate() {
        return requestDate;
    }

    public String getStatus() {
        return status;
    }
}
