package campusbooking;

import java.util.*;

//stores event information

public class Event {

    private String eventId;
    private String title;
    private String location;
    private int capacity;
//constuctor: Creates a new event with given details
    public Event(String eventId, String title, String location, int capacity) {
        this.eventId = eventId; //Unique identifier for the event
        this.title = title;
        this.location = location;
        this.capacity = capacity;
    }
// returns event id
    public String getEventId() {
        return eventId;
    }
//returns event title
    public String getTitle() {
        return title;
    }
    // returns location
    public String getLocation() {
        return location;
    }
// returns capacity
    public int getCapacity() {
        return capacity;
    }
}

