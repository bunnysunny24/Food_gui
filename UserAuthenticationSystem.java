import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationSystem {
    // This map can be used to cache user data in memory
    private Map<String, User> users = new HashMap<>();

    // Add a new user to the system
    public boolean addUser(User user) {
        if (user != null && user.getUsername() != null) {
            if (doesUserExist(user.getUsername())) {
                System.out.println("Failed to add user: Duplicate entry '" + user.getUsername() + "'");
                return false; // Indicate duplicate entry
            }
            users.put(user.getUsername(), user);
            System.out.println("User " + user.getUsername() + " added successfully.");
            return true; // Indicate successful addition
        } else {
            System.out.println("Failed to add user: User is null or username is null.");
            return false; // Indicate failure
        }
    }

    // Check if a user already exists
    private boolean doesUserExist(String username) {
        return users.containsKey(username);
    }

    // Authenticate a user based on username, password, and role
    public User authenticate(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            System.out.println("Authentication failed: Username, password, or role is null.");
            return null; // Invalid input
        }

        // Check the cache first
        User user = users.get(username);
        if (user != null) {
            if (user.getPassword().equals(password) && user.getRole().equalsIgnoreCase(role)) {
                System.out.println("User " + username + " authenticated successfully.");
                return user; // Return the authenticated user
            } else {
                System.out.println("Authentication failed: Incorrect password or role.");
            }
        } else {
            System.out.println("Authentication failed: User not found.");
        }
        return null; // Return null if authentication fails
    }

    // Additional methods for user management can be added here
}
