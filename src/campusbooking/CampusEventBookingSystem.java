package campusbooking;

import java.util.*;

public class CampusEventBookingSystem {

    private UserManager userManager;
    private EventManager eventManager;
    private BookingManager bookingManager;

    public CampusEventBookingSystem() {
        userManager = new UserManager();
        eventManager = new EventManager();
        bookingManager = new BookingManager();

        loadSampleData();
    }

    private void loadSampleData() {

        // USERS
        userManager.addUser(new Student("U1", "Mateen", "m@email.com"));
        userManager.addUser(new Staff("U2", "Alice", "a@email.com"));
        userManager.addUser(new Guest("U3", "Bob", "b@email.com"));

        // EVENTS
        eventManager.addEvent(new Concert("E1", "Music Night", "Hall A", 2));
        eventManager.addEvent(new Workshop("E2", "Coding Bootcamp", "Lab 1", 3));
        eventManager.addEvent(new Seminar("E3", "AI Talk", "Room 301", 1));
    }

    //  EVENTS
    public Collection<Event> listEvents() {
        return eventManager.getAllEvents();
    }

    //  USERS
    public Collection<User> listUsers() {
        return userManager.getAllUsers();
    }

    //  CREATE BOOKING
    public boolean createBooking(String userId, String eventId) {

        User u = userManager.getUser(userId);
        Event e = eventManager.getEvent(eventId);

        if (u == null || e == null) return false;

        String bookingId = "B" + (bookingManager.getAllBookings().size() + 1);

        return bookingManager.createBooking(bookingId, u, e);
    }

    //  CANCEL
    public boolean cancelBooking(String bookingId) {
        return bookingManager.cancelBooking(bookingId);
    }

    //  BOOKINGS
    public boolean hasBookings() {
        return bookingManager.hasBookings();
    }

    public Collection<Booking> getAllBookings() {
        return bookingManager.getAllBookings();
    }

    public List<Booking> getUserBookings(String userId) {
        return bookingManager.getBookingsForUser(userId);
    }
}