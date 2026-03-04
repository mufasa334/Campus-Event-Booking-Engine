package campusbooking;



import java.util.*;

public class BookingManager {

    private Map<String, Booking> bookings = new HashMap<>();

    private UserManager userManager;
    private EventManager eventManager;

    public BookingManager(UserManager u, EventManager e) {
        userManager = u;
        eventManager = e;
    }

    public Booking createBooking(String userId, String eventId) {

        User user = userManager.getUser(userId);
        Event event = eventManager.getEvent(eventId);

        Booking b = new Booking(userId, eventId);
        bookings.put(b.getBookingId(), b);

        return b;
    }

    public void cancelBooking(String bookingId) {

        Booking b = bookings.get(bookingId);

        if (b != null)
            b.setStatus("CANCELLED");
    }

    public Collection<Booking> listBookings() {
        return bookings.values();
    }
}