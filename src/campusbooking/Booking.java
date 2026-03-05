package campusbooking;

// represents a booking connecting a user to an event.
public class Booking {

    private static int counter = 1; //counter starts at 1 to make it easier to understand

    private String bookingId; //unique booking identifier
    private String userId;
    private String eventId;
    private String status;
    //creates a booking and automatically generates a unique booking id
    public Booking(String userId, String eventId) {
     //generate boooking id (B1,B2....)
        this.bookingId = "B" + counter++;
        this.userId = userId;
        this.eventId = eventId;
        this.status = "CONFIRMED"; //default status when booking is created
    }
//returns booking id
    public String getBookingId() {
        return bookingId;
    }
//returns uesr id
    public String getUserId() {
        return userId;
    }
//returns event id
    public String getEventId() {
        return eventId;
    }
// returns booking status
    public String getStatus() {
        return status;
    }
//Updates bookin status, used when cancelling
    public void setStatus(String status) {
        this.status = status;
    }
}