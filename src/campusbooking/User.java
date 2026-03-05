package campusbooking;


//Only stores data about a user.
//Users are stored in UserManager

public class User {

    private String userId;
    private String name;

// Constructor: creates a new User object with the given information.
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
    }
//returns user id
    public String getUserId() {
        return userId;
    }
//returns name
    public String getName() {
        return name;
    }
}
