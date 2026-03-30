package campusbooking;

public class Booking {
    private String bookingId;
    private User user;
    private Event event;

    public Booking(String id, User user, Event event) {
        this.bookingId = id;
        this.user = user;
        this.event = event;
    }

    public String getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public Event getEvent() { return event; }

    @Override
    public String toString() {
        return bookingId + " | " + user.getName() + " -> " + event.getTitle();
    }
}