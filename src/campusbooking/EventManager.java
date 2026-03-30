package campusbooking;

import java.util.*;

public class EventManager {
    private HashMap<String, Event> events = new HashMap<>();

    public void addEvent(Event e) {
        events.put(e.getEventId(), e);
    }

    public Event getEvent(String id) {
        return events.get(id);
    }

    public boolean exists(String id) {
        return events.containsKey(id);
    }

    public Collection<Event> getAllEvents() {
        return events.values();
    }
}