package campusbooking;

public class User {

    private String userId;
    private String name;


    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
