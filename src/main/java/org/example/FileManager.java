package org.example;

import java.util.ArrayList;
import java.util.List;

/*
 * Simple FileManager skeleton for Phase 1.
 * Manages lists of users and events only.
 */
public class FileManager {

    // Lists to store Users and Events
    private List<User> users;
    private List<Event> events;

    // Constructor initializes empty lists
    public FileManager() {
        users = new ArrayList<>();
        events = new ArrayList<>();
    }

    //-----------------------------------
    // USER MANAGEMENT
    //-----------------------------------

    public void addUser(User user) {
        users.add(user);
        System.out.println("Added user: " + user.getName());
    }

    public void listUsers() {
        System.out.println("Users in system:");
        for (User u : users) {
            System.out.println("- " + u.getUserId() + ": " + u.getName() + " (" + u.getUserType() + ")");
        }
    }

    //-----------------------------------
    // EVENT MANAGEMENT
    //-----------------------------------

    public void addEvent(Event event) {
        events.add(event);
        System.out.println("Added event: " + event.getTitle());
    }

    public void listEvents() {
        System.out.println("Events in system:");
        for (Event e : events) {
            System.out.println("- " + e.getEventId() + ": " + e.getTitle() + " [" + e.getEventType() + "]");
        }
    }
}