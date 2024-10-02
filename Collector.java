public class Collector extends User {
    private String name;

    public Collector(String username, String password, String name) {
        super(username, password, "collector");
        this.name = name;
    }

    // Additional methods can be added here
}
