package campusbooking;

public class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String role;

    public User(String userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return userId + " | " + name + " | " + role;
    }
}