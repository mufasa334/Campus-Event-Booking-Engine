package campusbooking;

import java.util.*;

public class Event {

    private String eventId;
    private String title;


    public Event(String eventId, String title, String location, int capacity) {
        this.eventId = eventId;
        this.title = title;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }
}

