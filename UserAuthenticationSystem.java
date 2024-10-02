import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationSystem {
    private Map<String, User> users = new HashMap<>();

    // Add a new user to the system
    public void addUser(User user) {
        if (user != null && user.getUsername() != null) {
            users.put(user.getUsername(), user);
            System.out.println("User " + user.getUsername() + " added successfully.");
        } else {
            System.out.println("Failed to add user: User is null or username is null.");
        }
    }

    // Authenticate a user based on username, password, and role
    public User authenticate(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            System.out.println("Authentication failed: Username, password, or role is null.");
            return null; // Invalid input
        }

        User user = users.get(username);
        if (user != null) {
            if (user.getPassword().equals(password) && user.getRole().equalsIgnoreCase(role)) {
                System.out.println("User " + username + " authenticated successfully.");
                return user;
            } else {
                System.out.println("Authentication failed: Incorrect password or role.");
            }
        } else {
            System.out.println("Authentication failed: User not found.");
        }
        return null; // Return null if authentication fails
    }

    // Retrieve a user by username
    public User getUser(String username) {
        return users.get(username);
    }

    // Check if a user exists in the system
    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }

    // Remove a user from the system
    public boolean removeUser(String username) {
        boolean removed = users.remove(username) != null; // Return true if user was removed
        if (removed) {
            System.out.println("User " + username + " removed successfully.");
        } else {
            System.out.println("Failed to remove user: User not found.");
        }
        return removed;
    }
}
