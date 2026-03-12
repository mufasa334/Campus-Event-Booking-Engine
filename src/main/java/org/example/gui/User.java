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

    //variable for limtingNumber methods
    private int limitingInt;

    //-----------------------------------CONSTRUCTOR--------------------------------------------

    public User(String userId, String name, String email, UserType userType, int maxConfirmedBookings) {

        setUserId(userId);
        setName(name);
        setEmail(email);
        setUserType(userType);
        setMaxConfirmedBookings(maxConfirmedBookings);
        setLimitingInt(limitingInt);

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

    public void setLimitingInt(int limitingInt) {
        limitingInt = 0;
        this.limitingInt = limitingInt;
    }

    public int getLimitingInt(){
        return limitingInt;
    }

    //---------------------------------METHODS----------------------------------------------------
    //methods to help with limiting the number of bookings per user
    public int limitingNumberUP(){
        limitingInt = limitingInt + 1;
        return limitingInt;
    }

    public int limitingNumberDOWN(){
        if (limitingInt > 0){
            limitingInt = limitingInt - 1;
            return limitingInt;
        }
        return limitingInt;
    }
    //A METHOD FOR EACH SUBCLASS THAT WILL PRINT OUT THEIR DETAILS
    //public abstract void details();

    //A METHOD FOR EACH SUBCLASS THAT WILL BOOK AN EVENT USING AN EMPTY BOOKING SLOT
    //PRECONDITION: ANY [EVENT] TO BE BOOKED WITH A BOOKING VARIABLE
    //public abstract void book(Event event);
}
