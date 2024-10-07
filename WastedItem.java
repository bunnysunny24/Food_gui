import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an item that has been wasted, including its name, quantity, unit, and the date it was wasted.
 */
public class WastedItem {
    private String name; // Name of the food item
    private BigDecimal quantity; // Quantity of the item wasted
    private String unit; // Unit of measurement for the quantity
    private LocalDate dateWasted; // Date the item was wasted

    // Default constructor for flexibility (if needed)
    public WastedItem() {
    }

    public WastedItem(String name, BigDecimal quantity, String unit, LocalDate dateWasted) {
        setName(name);
        setQuantity(quantity);
        setUnit(unit);
        setDateWasted(dateWasted);
    }

    // Getters
    public String getName() {
        return name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getDateWasted() {
        return dateWasted;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setQuantity(BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty.");
        }
        this.unit = unit;
    }

    public void setDateWasted(LocalDate dateWasted) {
        if (dateWasted == null) {
            throw new IllegalArgumentException("Date wasted cannot be null.");
        }
        this.dateWasted = dateWasted;
    }

    @Override
    public String toString() {
        return String.format("WastedItem{name='%s', quantity=%s, unit='%s', dateWasted=%s}",
                name, quantity, unit, dateWasted);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WastedItem)) return false;
        WastedItem that = (WastedItem) obj;
        return quantity.compareTo(that.quantity) == 0 &&
                name.equals(that.name) &&
                unit.equals(that.unit) &&
                dateWasted.equals(that.dateWasted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, unit, dateWasted);
    }
}
