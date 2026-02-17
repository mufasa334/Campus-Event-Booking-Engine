package org.example;
import java.util.ArrayList;

public class BookingManager {
    public ArrayList<String> UserList = new ArrayList<>();
    public int capacity;

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

    public String getStatus(String enteredName){
        for (int i = 0; i < capacity; i++) {
            if (enteredName.equals(UserList.get(i))){
                status = "Confirmed";
                return status;

            }
            if(UserList.get(i) == null){
                status = "Canceled";
                return status;
            }
        }
        for (int j = capacity; j < (j+1); j++){
            if (enteredName.equals(UserList.get(j))){
                status = "WaitListed";
                return status;

            }
            if (UserList.get(j) == null){
                status = "Canceled";
                return status;

            }
        }
        status = "Canceled";
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }


    //constructor
    public BookingManager (int capacity, String EventID, String EventName){
        this.capacity = capacity;
        this.EventID = EventID;
        this.EventName = EventName;
        UserList.add(null);
    }

    //methods
    public void addUser(String enteredUserName){
        for (int i = 0; i < (i+1); i++) {
            if (UserList.get(i) == null){
                UserList.set(i, enteredUserName);
                break;
            }
            if(enteredUserName.equals(UserList.get(i))) {
                System.out.println("User Is Already Listed"); //would need to print to UI
                break;
            }
        }
        UserList.add(null);

    }

    public void cancelBooking(String enteredUserName){
        for (int i = 0; i < capacity; i++) {
            if (enteredUserName.equals(UserList.get(i))){
                UserList.remove(i);
            }
            if(UserList.get(i) == null){
                break;
            }
        }
        for (int j = capacity; j < (j+1); j++){
            if (enteredUserName.equals(UserList.get(j))){
                UserList.remove(j);
            }
            if (UserList.get(j) == null){
                break;

            }
        }
    }

    //note that a lot of the prints in this part will need to be printed/shown in the UI later not in the terminal
    public void bookWaitlistPrint(){
        System.out.println("Event Name/ID: " + EventName + "/" + EventID);
        System.out.println("Users Booked: ");
        for (int i = 0; i < capacity; i++) {
            if (UserList.get(i) != null){
                System.out.println(UserList.get(i));
            }
            if(UserList.get(i) == null){
                break;
            }
        }
        System.out.println("Users Waitlisted: ");
        for (int j = capacity; j < (j+1); j++){
            if (UserList.get(j) != null){
                System.out.println(UserList.get(j));
            }
            if (UserList.get(j) == null){
                break;

            }
        }
    }

}
