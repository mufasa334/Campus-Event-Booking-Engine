package campusbooking;

import java.util.*;
//stores events and allows system to add events, retrieve events, and list all events
//Events are stored in hashmap
public class EventManager {
//hashmap
    private Map<String, Event> events = new HashMap<>();
//adds event to the system, the eventID is the key to object
    public void addEvent(Event event) {
        events.put(event.getEventId(), event);
    }
//retrieves event using id, returns null if event doesn't exist
    public Event getEvent(String id) {
        return events.get(id);
    }
//returns all events
    public Collection<Event> listEvents() {
        return events.values();
    }
}