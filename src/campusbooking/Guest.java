package campusbooking;

public class Guest extends User {
    public Guest(String id, String name, String email) {
        super(id, name, email, "Guest");
    }
}