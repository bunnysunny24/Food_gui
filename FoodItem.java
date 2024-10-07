import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a food item that has been wasted, including its name, quantity, unit, and the date it was wasted.
 */
public class FoodItem {
    private String name; // Name of the food item
    private double quantity; // Quantity of the item wasted
    private String unit; // Unit of measurement for the quantity
    private LocalDate dateWasted; // Date the item was wasted

    // Constructor with validation
    public FoodItem(String name, double quantity, String unit, LocalDate dateWasted) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty.");
        }
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.dateWasted = dateWasted; // Initialize the dateWasted
    }

    // Getters
    public String getName() {
        return name; // Return the name of the food item
    }

    public double getQuantity() {
        return quantity; // Return the quantity of the food item
    }

    public String getUnit() {
        return unit; // Return the unit of the food item
    }

    public LocalDate getDateWasted() {
        return dateWasted; // Return the date when the item was wasted
    }

    // Setters
    public void setQuantity(double quantity) {
        if (quantity >= 0) {
            this.quantity = quantity; // Update quantity if it's valid
        } else {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
    }

    // Method to update the dateWasted if needed
    public void updateDateWasted(LocalDate newDateWasted) {
        if (newDateWasted != null) {
            this.dateWasted = newDateWasted; // Update the date when the item was wasted
        } else {
            throw new IllegalArgumentException("Date wasted cannot be null.");
        }
    }

    // Override equals and hashCode for proper comparison in collections
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FoodItem foodItem = (FoodItem) obj;
        return Double.compare(foodItem.quantity, quantity) == 0 &&
                name.equals(foodItem.name) &&
                unit.equals(foodItem.unit) &&
                dateWasted.equals(foodItem.dateWasted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, unit, dateWasted);
    }

    // String representation of the food item
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return name + " - " + quantity + " " + unit + " (Wasted on: " + dateWasted.format(formatter) + ")";
    }
}
