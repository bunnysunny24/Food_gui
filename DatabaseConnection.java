import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/food_application"; // Update with your DB URL
    private static final String USER = "root"; // Update with your DB username
    private static final String PASSWORD = "Bunny$7890"; // Update with your DB password

    public static Connection getConnection() throws SQLException {
        // Load the MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Make sure you have the driver in your classpath.");
            e.printStackTrace();
            return null; // Return null if the driver is not found
        }

        // Create and return the connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to test the connection
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Connection to the database established successfully!");
                return true; // Connection successful
            } else {
                System.out.println("Failed to establish a connection to the database.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred while testing connection:");
            e.printStackTrace();
        }
        return false; // Connection failed
    }
}
