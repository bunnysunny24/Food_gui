import java.time.LocalDate;

public class FoodItem {
    private String name;
    private double quantity; // Changed to double for consistency with potential waste scenarios
    private String unit;
    private LocalDate dateWasted; // Field to store the date when the item was wasted

    // Constructor
    public FoodItem(String name, double quantity, String unit, LocalDate dateWasted) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.dateWasted = dateWasted; // Initialize the dateWasted
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for quantity
    public double getQuantity() {
        return quantity;
    }

    // Getter for unit
    public String getUnit() {
        return unit;
    }

    // Getter for dateWasted
    public LocalDate getDateWasted() {
        return dateWasted; // Return the date when the item was wasted
    }

    @Override
    public String toString() {
        return name + " - " + quantity + " " + unit + " (Wasted on: " + dateWasted + ")";
    }
}
