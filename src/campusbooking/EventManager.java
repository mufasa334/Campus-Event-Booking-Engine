package campusbooking;

import java.util.*;

public class EventManager {

    private Map<String, Event> events = new HashMap<>();

    public void addEvent(Event event) {
        events.put(event.getEventId(), event);
    }

    public Event getEvent(String id) {
        return events.get(id);
    }

    public Collection<Event> listEvents() {
        return events.values();
    }
}