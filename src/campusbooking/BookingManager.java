package campusbooking;

import java.util.*;

public class BookingManager {

    private HashMap<String, Booking> bookings = new HashMap<>();

    public boolean hasBookings() {
        return !bookings.isEmpty();
    }

    public boolean createBooking(String bookingId, User user, Event event) {

        if (bookings.containsKey(bookingId)) {
            System.out.println("Booking ID already exists.");
            return false;
        }

        // prevent duplicate booking
        for (Booking b : bookings.values()) {
            if (b.getUser().getUserId().equals(user.getUserId()) &&
                    b.getEvent().getEventId().equals(event.getEventId())) {
                System.out.println("User already booked this event.");
                return false;
            }
        }

        // capacity check
        if (!event.addUser(user.getUserId())) {
            System.out.println("Event is full.");
            return false;
        }

        bookings.put(bookingId, new Booking(bookingId, user, event));
        return true;
    }

    public boolean cancelBooking(String bookingId) {

        Booking b = bookings.get(bookingId);
        if (b == null) return false;

        b.getEvent().removeUser(b.getUser().getUserId());
        bookings.remove(bookingId);

        return true;
    }

    public Collection<Booking> getAllBookings() {
        return bookings.values();
    }

    public List<Booking> getBookingsForUser(String userId) {
        List<Booking> result = new ArrayList<>();

        for (Booking b : bookings.values()) {
            if (b.getUser().getUserId().equals(userId)) {
                result.add(b);
            }
        }

        return result;
    }
}
