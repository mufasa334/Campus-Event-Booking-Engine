package org.example.gui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BookingWaitlistingManager {

    // Store full User objects instead of just names
    private List<User> userList = new ArrayList<>();
    private int capacity;

    private LocalDateTime createdAt;
    private String eventId;
    private String eventName;
    private String bookingId;
    private String status;

    // ---------------------- CONSTRUCTOR ----------------------

    public BookingWaitlistingManager(int capacity, String eventId, String eventName) {
        this.capacity = capacity;
        this.eventId = eventId;
        this.eventName = eventName;

        int bookingIdInt = ThreadLocalRandom.current().nextInt(999999, 10000000);
        this.bookingId = String.valueOf(bookingIdInt);
        this.createdAt = LocalDateTime.now();
    }

    // ---------------------- GETTERS / SETTERS ----------------------

    public List<User> getUserList() {
        return userList;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEventID() {
        return eventId;
    }

    public void setEventID(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getBookingID() {
        return bookingId;
    }

    public void setBookingID(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRawStatusField() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ---------------------- HELPER METHODS ----------------------

    public User getUserByIndex(int index) {
        if (index < 0 || index >= userList.size()) {
            return null;
        }
        return userList.get(index);
    }

    public boolean containsUser(User user) {
        if (user == null) return false;

        for (User existingUser : userList) {
            if (existingUser.getUserId().equals(user.getUserId())) {
                return true;
            }
        }
        return false;
    }

    public String getStatus(User user) {
        if (user == null) {
            return "Canceled";
        }

        int position = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId().equals(user.getUserId())) {
                position = i;
                break;
            }
        }

        if (position == -1) {
            return "Canceled";
        }

        if (position < capacity) {
            return "Booked";
        } else {
            return "Waitlisted";
        }
    }

    public String getStatusByUserId(String userId) {
        for (User user : userList) {
            if (user.getUserId().equals(userId)) {
                return getStatus(user);
            }
        }
        return "Canceled";
    }

    // ---------------------- MAIN LOGIC ----------------------

    public void addUser(User user) {
        if (user == null) {
            System.out.println("Cannot add null user.");
            return;
        }

        if (containsUser(user)) {
            System.out.println("User is already listed.");
            return;
        }

        userList.add(user);
    }

    public void cancelBooking(User user) {
        if (user == null) return;

        userList.removeIf(existingUser -> existingUser.getUserId().equals(user.getUserId()));
    }

    public void bookWaitlistPrint() {
        System.out.println("Event Name/ID: " + eventName + "/" + eventId);
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Event Created On: " + createdAt);

        System.out.println("Users Booked:");
        int confirmedCount = Math.min(capacity, userList.size());
        for (int i = 0; i < confirmedCount; i++) {
            User user = userList.get(i);
            if (user != null) {
                System.out.println(user.getUserId() + " - " + user.getName());
            }
        }

        System.out.println("Users Waitlisted:");
        for (int i = capacity; i < userList.size(); i++) {
            User user = userList.get(i);
            if (user != null) {
                System.out.println(user.getUserId() + " - " + user.getName());
            }
        }
    }
}