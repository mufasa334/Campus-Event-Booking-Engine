package campusbooking;

public class CampusEventBookingSystem {

    private UserManager userManager = new UserManager();
    private EventManager eventManager = new EventManager();
    private BookingManager bookingManager =
            new BookingManager(userManager, eventManager);

    public UserManager users() {
        return userManager;
    }

    public EventManager events() {
        return eventManager;
    }

    public BookingManager bookings() {
        return bookingManager;
    }

    public void loadSampleData() {

        userManager.addUser(new User("U1:","Alice","alice@guelph.ca"));
        userManager.addUser(new User("U2:","Bob","bob@guelph.ca"));

        eventManager.addEvent(new Event("E1:","Java Workshop","SSC",20));
        eventManager.addEvent(new Event("E2:","AI Seminar","THRN",30));
    }
}