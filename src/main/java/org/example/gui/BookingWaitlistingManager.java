package org.example.gui;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class BookingWaitlistingManager {
    public ArrayList<User> UserList = new ArrayList<>();
    public int capacity;

    private LocalDateTime CreatedAt;
    private String BookingID;

    //---------------------------------CONSTRUCTOR------------------------------------------------

    public BookingWaitlistingManager (int capacity){
        this.capacity = capacity;
        BookingID = String.valueOf(ThreadLocalRandom.current().nextInt(999999, 10000000));
        CreatedAt = LocalDateTime.now();
        // UserList.add(null);
    }

    //--------------------------------GETTERS/SETTERS------------------------------------------

    public String getBookingID(){
        return BookingID;
    }

    public void setBookingID(String BookingID){
        this.BookingID = BookingID;
    }

    public LocalDateTime getCreatedAt(){
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime CreatedAt){
        this.CreatedAt = CreatedAt;
    }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus(User user) {
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


    //-----------------------------------METHODS----------------------------------


    // Fixed: Changed (i+1) infinite loop to check actual list size
    public void addUser(User user) {
        if (UserList.contains(user)) {
            System.out.println("User Is Already Listed");
            return;
        }
        UserList.add(user);
    }

    public void cancelBooking(User user){
        // no need a loop to remove someone, arraylist are smart
        user.limitingNumberDOWN();
        UserList.remove(user);
    }

    //note that a lot of the prints in this part will need to be printed/shown in the UI later not in the terminal
    /*public void bookWaitlistPrint(){
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
    }*/

}
