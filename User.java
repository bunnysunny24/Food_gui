public class User {
    private String username;
    private String password;
    private String role;
    private String name; // Property to hold the user's name

    // Constructor to initialize all fields
    public User(String username, String password, String role, String name) {
        // Validate inputs before assigning
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role. Role must be 'Collector' or 'Provider'.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        this.username = username; // Assign username
        this.password = password; // Assign password
        this.role = role;         // Assign role
        this.name = name;         // Assign name
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Getter for role
    public String getRole() {
        return role;
    }

    // Getter for name
    public String getName() {
        return name; // Return name
    }

    // Setter for name
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name; // Update name
    }

    // Validate if the role is either 'Collector' or 'Provider'
    private boolean isValidRole(String role) {
        return "Collector".equalsIgnoreCase(role) || "Provider".equalsIgnoreCase(role);
    }

    // Override toString for easier debugging/logging
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
