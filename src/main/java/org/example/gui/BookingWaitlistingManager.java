package org.example.gui;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class BookingWaitlistingManager {
    public ArrayList<String> UserList = new ArrayList<>();
    public int capacity;

    private LocalDateTime CreatedAt;
    private String UserID;
    private String UserName;
    private String EventID;
    private String EventName;
    private String BookingID;
    private String status;

    //getter and setters

    //the UserID stuff seems unnecessary if all that is really needed is the Username
    public String getUserID(int numberInList){
        UserID = UserList.get(numberInList);
        //would need some conversion factor here to check what the userID goes with what username, and set the new UserID to that
        return UserID;
    }
    public void setUserID(String UserID){
        this.UserID = UserID;
    }
    public String getUserName(int numberInList) {
        UserName = UserList.get(numberInList);
        return UserName;
    }

    public void setUserName(String UserName){
       this.UserName = UserName;
    }

    public String getEventID(){
        return EventID;
    }

    public void setEventID(String EventID){
        this.EventID = EventID;
    }

    public String getEventName(){
        return EventName;
    }

    public void setEventName(String EventName){
        this.EventName = EventName;
    }

    public String getBookingID(){
        return BookingID;
    }

    public void setBookingID(String BookingID){
        this.BookingID = BookingID;
    }

    public String getStatus(String user) {
        // 1. If the name isn't even in the list, they are definitely canceled/not booked
        if (!UserList.contains(user)) {
            return "Canceled";
        }

        // 2. Find where they are in the list
        int position = UserList.indexOf(user);

        // 3. Compare their position to the capacity
        // If they are in position 0-49 (for a 50 cap event), they are "Booked"
        if (position < capacity) {
            return "Booked";
        } else {
            return "Waitlisted";
        }
    }

    public void setStatus(String status){
        this.status = status;
    }

    public LocalDateTime getCreatedAt(){
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime CreatedAt){
        this.CreatedAt = CreatedAt;
    }


    //constructor
    public BookingWaitlistingManager (int capacity, String EventID, String EventName){
        this.capacity = capacity;
        this.EventID = EventID;
        this.EventName = EventName;

        int BookingIDInt = ThreadLocalRandom.current().nextInt(999999, 10000000);
        BookingID = String.valueOf(BookingIDInt);
        CreatedAt = LocalDateTime.now();
       // UserList.add(null);
    }

    //methods
    // Fixed: Changed (i+1) infinite loop to check actual list size
    public void addUser(String enteredUserName) {
        if (UserList.contains(enteredUserName)) {
            System.out.println("User Is Already Listed");
            return;
        }
        UserList.add(enteredUserName);
    }

    public void cancelBooking(String enteredUserName){
        // no need a loop to remove someone, arraylist are smart
        UserList.remove(enteredUserName);
    }

    //note that a lot of the prints in this part will need to be printed/shown in the UI later not in the terminal
    public void bookWaitlistPrint(){
        System.out.println("Event Name/ID: " + EventName + "/" + EventID);
        System.out.println("Booking ID: " + BookingID);
        System.out.println("Event Created On: " + CreatedAt);

        System.out.println("Users Booked: ");
        // Math.min forces it to stop checking if the list is shorter than the capacity
        int confirmedCount = Math.min(capacity + 1, UserList.size());
        for (int i = 0; i < confirmedCount; i++) {
            if (UserList.get(i) != null){
                System.out.println(UserList.get(i));
            }
        }

        System.out.println("Users Waitlisted: ");
        // This safely prints anyone who got added after capacity was reached
        for (int j = capacity + 1; j < UserList.size(); j++){
            if (UserList.get(j) != null){
                System.out.println(UserList.get(j));
            }
        }
    }

}
