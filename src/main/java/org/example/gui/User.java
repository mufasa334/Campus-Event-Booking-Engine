package org.example.gui;
public abstract class User {

    public enum UserType {
        GUEST,
        STUDENT,
        STAFF
    }

    private int maxConfirmedBookings;
    private String userId;
    private String name;
    private String email;
    private UserType userType;

    //-----------------------------------CONSTRUCTOR--------------------------------------------

    public User(String userId, String name, String email, UserType userType, int maxConfirmedBookings) {

        setUserId(userId);
        setName(name);
        setEmail(email);
        setUserType(userType);
        setMaxConfirmedBookings(maxConfirmedBookings);

    }

    //---------------------------------SETTERS / GETTERS------------------------------------------

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setMaxConfirmedBookings(int maxConfirmedBookings) { this.maxConfirmedBookings = maxConfirmedBookings; }

    public int getMaxConfirmedBookings() { return maxConfirmedBookings; }

    public void setUserType(UserType userType) { this.userType = userType; }

    public UserType getUserType() { return userType; }

    //---------------------------------METHODS----------------------------------------------------

    //A METHOD FOR EACH SUBCLASS THAT WILL PRINT OUT THEIR DETAILS
    //public abstract void details();

    //A METHOD FOR EACH SUBCLASS THAT WILL BOOK AN EVENT USING AN EMPTY BOOKING SLOT
    //PRECONDITION: ANY [EVENT] TO BE BOOKED WITH A BOOKING VARIABLE
    //public abstract void book(Event event);
}
