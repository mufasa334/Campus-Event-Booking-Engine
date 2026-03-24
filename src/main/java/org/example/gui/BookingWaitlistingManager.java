package org.example.gui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BookingWaitlistingManager {

    public enum BookingStatus {
        CONFIRMED,
        WAITLISTED,
        CANCELLED
    }

    public class BookingEntry {
        private String bookingId;
        private User user;
        private LocalDateTime createdAt;
        private BookingStatus status;

        public BookingEntry(User user) {
            this.bookingId = String.valueOf(ThreadLocalRandom.current().nextInt(999999, 10000000));
            this.user = user;
            this.createdAt = LocalDateTime.now();
            this.status = BookingStatus.WAITLISTED;
        }

        public String getBookingId() { return bookingId; }
        public void setBookingId(String bookingId) { this.bookingId = bookingId; }

        public User getUser() { return user; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public BookingStatus getStatus() { return status; }
        public void setStatus(BookingStatus status) { this.status = status; }
    }

    private List<BookingEntry> bookings = new ArrayList<>();
    private int capacity;

    private LocalDateTime createdAt;
    private String eventId;
    private String eventName;

    public BookingWaitlistingManager(int capacity, String eventId, String eventName) {
        this.capacity = capacity;
        this.eventId = eventId;
        this.eventName = eventName;
        this.createdAt = LocalDateTime.now();
    }

    // ==============================
    // BASIC GETTERS
    // ==============================

    public List<BookingEntry> getBookings() { return bookings; }
    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getEventID() { return eventId; }
    public String getEventName() { return eventName; }

    // ==============================
    // CORE FUNCTIONS
    // ==============================

    public boolean containsUser(User user) {
        if (user == null) return false;

        for (BookingEntry booking : bookings) {
            if (booking.getUser().getUserId().equals(user.getUserId())
                    && booking.getStatus() != BookingStatus.CANCELLED) {
                return true;
            }
        }
        return false;
    }

    public BookingEntry findBookingByUser(User user) {
        if (user == null) return null;

        BookingEntry cancelledMatch = null;

        for (BookingEntry booking : bookings) {
            if (booking.getUser().getUserId().equals(user.getUserId())) {

                if (booking.getStatus() != BookingStatus.CANCELLED) {
                    return booking; // prefer active booking
                }

                cancelledMatch = booking;
            }
        }

        return cancelledMatch;
    }

    public void addUser(User user) {
        if (user == null) {
            System.out.println("Cannot add null user.");
            return;
        }

        for (BookingEntry booking : bookings) {
            if (booking.getUser().getUserId().equals(user.getUserId())) {

                if (booking.getStatus() != BookingStatus.CANCELLED) {
                    System.out.println("User already has an active booking.");
                    return;
                }

                // Rebook = treat like a new request at the end of the queue
                booking.setCreatedAt(LocalDateTime.now());
                booking.setStatus(BookingStatus.WAITLISTED);

                bookings.remove(booking);
                bookings.add(booking);
                return;
            }
        }

        bookings.add(new BookingEntry(user));
    }

    public void addLoadedBooking(User user, String bookingId, LocalDateTime createdAt, BookingStatus status) {
        BookingEntry booking = new BookingEntry(user);
        booking.setBookingId(bookingId);
        booking.setCreatedAt(createdAt);
        booking.setStatus(status);
        bookings.add(booking);
    }

    public boolean cancelBooking(User user) {
        BookingEntry booking = findBookingByUser(user);

        if (booking == null) return false;
        if (booking.getStatus() == BookingStatus.CANCELLED) return false;

        booking.setStatus(BookingStatus.CANCELLED);
        return true;
    }

    public boolean deleteBooking(User user) {
        BookingEntry booking = findBookingByUser(user);

        if (booking == null) return false;

        return bookings.remove(booking);
    }

    public String getStatus(User user) {
        BookingEntry booking = findBookingByUser(user);
        if (booking == null) return "Not Found";

        return switch (booking.getStatus()) {
            case CONFIRMED -> "Booked";
            case WAITLISTED -> "Waitlisted";
            case CANCELLED -> "Cancelled";
        };
    }

    // ==============================
    // DEBUG PRINT
    // ==============================

    public void bookWaitlistPrint() {
        System.out.println("Event Name/ID: " + eventName + "/" + eventId);

        for (BookingEntry booking : bookings) {
            System.out.println(
                    booking.getBookingId() + " | " +
                            booking.getUser().getUserId() + " - " +
                            booking.getUser().getName() + " | " +
                            booking.getStatus()
            );
        }
    }
}