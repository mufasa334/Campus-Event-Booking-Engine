import java.time.LocalDateTime;

public abstract class Event {

    public enum EventStatus {
        CONFIRMED,
        WAITLISTED,
        CANCELLED
    }

    public enum EventType {
        WORKSHOP,
        CONCERT,
        SEMINAR
    }

    private String eventId;
    private int capacity;
    private String location;
    private String title;
    private LocalDateTime dateTime;
    private EventStatus status;
    private EventType eventType;
    //private User[] attendees;
    //private int count = 0;
    //Maybe make an arraylist for the waitlist or something
    //-----------------------------------CONSTRUCTOR--------------------------------------------------

    public Event(String eventId, String title, LocalDateTime dateTime, String location, int capacity, EventType eventType) {

        setEventId(eventId);
        setTitle(title);
        setDateTime(dateTime);
        setLocation(location);
        setCapacity(capacity);
        setStatus(EventStatus.CONFIRMED);
        setEventType(eventType);

        //attendees = new User[capacity];
    }

    //----------------------------SETTERS / GETTERS-------------------------------------------------

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public LocalDateTime getDateTime() { return dateTime; }

    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public EventType getEventType() {return eventType;}

    //--------------------------------------METHODS-------------------------------------------------

    //A METHOD FOR EACH SUBCLASS THAT ADDS PEOPLE TO THE ATTENDANCE LIST AS LONG AS THE EVENT IS NOT FULL ON CAPACITY
    //PRECONDITION: ANY [PERSON] AND ADDS THEM TO THE ATTENDEES ARRAY
    /*public void addAttendance(User guy) {
        if(hasSpace()) {
            attendees[count] = guy;
            count++;
        }
        //You can put something about the waitlist here
    }

    //A METHOD THAT WILL RETURN TRUE IF THERE IS EMPTY SPACE IN THE ATTENDANCE ARRAY
    //POSTCONDITION: RETURNS [BOOLEAN] ABOUT WHETHER THERE IS EMPTY SPACE OR NOT
    public boolean hasSpace() {
        if (count < capacity) return true;
        return false;
    }*/

}
