package campusbooking;

import java.util.*;
//storage layer for users, has methods to add retrieve and list users
//Users stored in hashmap : key = userID, value = user object
public class UserManager {
//hashmap storing all users in the system
    private Map<String, User> users = new HashMap<>();
//adds a user, the user id is used as a key in the Hashmap
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
//retrieves a user by their key, returns null if user doesn't exist
    public User getUser(String id) {
        return users.get(id);
    }
//returns a collection of all users in the system
    public Collection<User> listUsers() {
        return users.values();
    }
}