package org.example;
import java.util.ArrayList;

public class BookingManager {
    public ArrayList<String> UserList = new ArrayList<>();
    public int numberInList;
    public String enteredID;
    public int capacity;

    private String UserID;
    private String EventID;
    private String BookingID;
    private String status;

    //getter and setters

    public String getUserID(int numberInList) {
        UserID = UserList.get(numberInList);
        return UserID;
    }

    public void setUserID(String UserID){
       this.UserID = UserID;
    }

    public String getEventID(){
        return EventID;
    }

    public void setEventID(String EventID){
        this.EventID = EventID;
    }

    public String getBookingID(){
        return BookingID;
    }

    public void setBookingID(String BookingID){
        this.BookingID = BookingID;
    }

    public String getStatus(String enteredID){
        for (int i = 0; i < capacity; i++) {
            if (enteredID.equals(UserList.get(i))){
                status = "Confirmed";
                return status;

            }
            if(UserList.get(i) == null){
                status = "Canceled";
                return status;
            }
        }
        for (int j = capacity; j < (j+1); j++){
            if (enteredID.equals(UserList.get(j))){
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
    public BookingManager (int capacity){
        this.capacity = capacity;
    }

    //methods


}
