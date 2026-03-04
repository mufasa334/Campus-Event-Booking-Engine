package campusbooking;

public class Booking {

    private static int counter = 1;

    private String bookingId;
    private String userId;
    private String eventId;
    private String status;

    public Booking(String userId, String eventId) {
        this.bookingId = "B" + counter++;
        this.userId = userId;
        this.eventId = eventId;
        this.status = "CONFIRMED";
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}