public class User {
    private String username;
    private String password;
    private String role;
    private String name; // Add name property

    // Updated constructor to initialize name
    public User(String username, String password, String role, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name; // Initialize name
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
        return name; // Add this getter
    }

    // Setter for name
    public void setName(String name) {
        this.name = name; // Add this setter
    }
}
