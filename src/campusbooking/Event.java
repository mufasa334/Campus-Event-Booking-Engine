package campusbooking;

import java.util.*;

public class Event {
    private String eventId;
    private String title;
    private String location;
    private int capacity;
    private ArrayList<String> confirmedUsers;

    public Event(String id, String title, String location, int capacity) {
        this.eventId = id;
        this.title = title;
        this.location = location;
        this.capacity = capacity;
        this.confirmedUsers = new ArrayList<>();
    }

    public String getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public ArrayList<String> getConfirmed() { return confirmedUsers; }

    public boolean addUser(String userId) {
        if (confirmedUsers.size() >= capacity) return false;
        if (confirmedUsers.contains(userId)) return false;

        confirmedUsers.add(userId);
        return true;
    }

    public boolean removeUser(String userId) {
        return confirmedUsers.remove(userId);
    }
}