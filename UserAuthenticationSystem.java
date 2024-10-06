import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationSystem {
    // This map is used to cache user data in memory
    private Map<String, User> users = new HashMap<>();
    
    // SQL queries
    private static final String INSERT_USER_SQL = "INSERT INTO users (username, password, role, name) VALUES (?, ?, ?, ?)";
    private static final String SELECT_USER_SQL = "SELECT * FROM users WHERE username = ?";
    private static final String AUTHENTICATE_USER_SQL = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";

    // Add a new user to the system
    public boolean addUser(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            System.out.println("Failed to add user: User is null or username/password is null.");
            return false;
        }

        // Log the attempt to add user
        System.out.println("Attempting to add user: " + user.getUsername());
        System.out.println("User role being added: '" + user.getRole() + "'");

        // Check if the role is valid
        if (!isValidRole(user.getRole())) {
            System.out.println("Failed to add user: Invalid role '" + user.getRole() + "'");
            return false; // Indicate invalid role
        }

        // Check if user exists in the cache or the database
        if (doesUserExist(user.getUsername()) || doesUserExistInDatabase(user.getUsername())) {
            System.out.println("Failed to add user: Duplicate entry '" + user.getUsername() + "'");
            return false; // Indicate duplicate entry
        }

        // Attempt to insert into the database
        if (addUserToDatabase(user)) {
            // Cache the user in memory
            users.put(user.getUsername(), user);
            System.out.println("User " + user.getUsername() + " added successfully.");
            return true; // Indicate successful addition
        } else {
            System.out.println("Failed to add user to the database.");
        }
        return false; // Indicate failure
    }

    // Check if a user already exists in the cache
    private boolean doesUserExist(String username) {
        return users.containsKey(username);
    }

    // Check if a user already exists in the database
    private boolean doesUserExistInDatabase(String username) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_USER_SQL)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next(); // Returns true if a record is found
            System.out.println("Checking database for user: " + username + " - Exists: " + exists);
            return exists;
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return false; // Return false if user doesn't exist or an error occurs
    }

    // Authenticate a user based on username, password, and role
    public User authenticate(String username, String password, String role) {
        if (username == null || password == null || role == null || !isValidRole(role)) {
            System.out.println("Authentication failed: Invalid input.");
            return null; // Invalid input
        }

        System.out.println("Attempting to authenticate user: " + username + " with role: " + role);

        // Check the cache first
        User user = users.get(username);
        if (user != null) {
            if (user.getPassword().equals(password) && user.getRole().equalsIgnoreCase(role)) {
                System.out.println("User " + username + " authenticated successfully from cache.");
                return user; // Return the authenticated user
            } else {
                System.out.println("Cache check failed: Incorrect password or role for user " + username);
            }
        } else {
            // Fallback: check the database if user is not found in the cache
            user = authenticateFromDatabase(username, password, role);
            if (user != null) {
                // Cache the authenticated user
                users.put(username, user);
                System.out.println("User " + username + " authenticated from the database and cached.");
                return user;
            } else {
                System.out.println("Authentication failed: User not found in the database for " + username);
            }
        }
        return null; // Return null if authentication fails
    }

    // Authenticate a user from the database
    private User authenticateFromDatabase(String username, String password, String role) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(AUTHENTICATE_USER_SQL)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            System.out.println("Executing query: " + statement.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Create and return a User object based on the database record
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null; // Return null if authentication fails in the database
    }

    // Method to add user to the database
    private boolean addUserToDatabase(User user) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword()); // Consider hashing the password before storing it
            statement.setString(3, user.getRole());
            statement.setString(4, user.getName());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User " + user.getUsername() + " inserted into database.");
                return true; // User was successfully added to the database
            } else {
                System.out.println("No rows inserted for user: " + user.getUsername());
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return false; // User could not be added
    }

    // Validate if the role is valid
    private boolean isValidRole(String role) {
        System.out.println("Validating role: '" + role + "'");
        boolean isValid = "Collector".equalsIgnoreCase(role) || "Provider".equalsIgnoreCase(role);
        System.out.println("Is role valid? " + isValid);
        return isValid;
    }

    // Handle SQL exceptions
    private void handleSQLException(SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace(); // Log the stack trace for debugging
    }

    // Additional methods for user management can be added here
}
