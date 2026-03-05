package campusbooking;



import java.util.*;
//depends on userManager and EventManager

public class BookingManager {
//stores all bookings
    private Map<String, Booking> bookings = new HashMap<>();
//references to event and user managers
    private UserManager userManager;
    private EventManager eventManager;
//receives user and event managers so that bookings can validate users and events
    public BookingManager(UserManager u, EventManager e) {
        userManager = u;
        eventManager = e;
    }
//creates new booking
    public Booking createBooking(String userId, String eventId) {
//retrieve user and events
        User user = userManager.getUser(userId);
        Event event = eventManager.getEvent(eventId);
//create the booking
        Booking b = new Booking(userId, eventId);
        //store booking in hashmap
        bookings.put(b.getBookingId(), b);

        return b;
    }
//cancels booking
    public void cancelBooking(String bookingId) {

        Booking b = bookings.get(bookingId);

        if (b != null)
            b.setStatus("CANCELLED");
    }
//returns all bookings in Hashmap
    public Collection<Booking> listBookings() {
        return bookings.values();
    }
}