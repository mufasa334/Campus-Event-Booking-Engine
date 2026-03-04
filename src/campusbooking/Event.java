package campusbooking;

import java.util.*;

public class Event {

    private String eventId;
    private String title;
    private String location;
    private int capacity;

    public Event(String eventId, String title, String location, int capacity) {
        this.eventId = eventId;
        this.title = title;
        this.location = location;
        this.capacity = capacity;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }
    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }
}

