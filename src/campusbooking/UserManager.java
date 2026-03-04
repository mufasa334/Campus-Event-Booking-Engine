package campusbooking;

import java.util.*;

public class UserManager {

    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public Collection<User> listUsers() {
        return users.values();
    }
}