import java.time.LocalDate;
import java.util.Objects;

public class WastedItem {
    private String name;
    private double quantity; // Changed back to double for consistency
    private LocalDate dateWasted;

    public WastedItem(String name, double quantity, LocalDate dateWasted) {
        this.name = name;
        this.quantity = quantity;
        this.dateWasted = dateWasted;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public LocalDate getDateWasted() {
        return dateWasted;
    }

    // Setters (optional)
    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setDateWasted(LocalDate dateWasted) {
        this.dateWasted = dateWasted;
    }

    @Override
    public String toString() {
        return "WastedItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", dateWasted=" + dateWasted +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WastedItem that = (WastedItem) obj;
        return Double.compare(that.quantity, quantity) == 0 &&
                name.equals(that.name) &&
                dateWasted.equals(that.dateWasted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, dateWasted);
    }
}
