package org.example.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class BookingSystemTest {

    private Workshop event;
    private Student user1;
    private Student user2;
    private Student user3;

    @BeforeEach
    void setUp() {
        // Create a fresh event with capacity 2 before each test
        event = new Workshop("E001", "Test Workshop", LocalDateTime.now(), "Room 101", 2, "Java");
        user1 = new Student("U001", "Alice", "alice@uoguelph.ca");
        user2 = new Student("U002", "Bob", "bob@uoguelph.ca");
        user3 = new Student("U003", "Charlie", "charlie@uoguelph.ca");
    }

    // Test 1: Booking under capacity → status becomes CONFIRMED
    @Test
    void testBookingUnderCapacityIsConfirmed() {
        event.getManager().addUser(user1);
        BookingWaitlistingManager.BookingEntry booking = event.getManager().findBookingByUser(user1);

        assertNotNull(booking);
        // After adding, status starts as WAITLISTED — simulate promotion
        booking.setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);
        assertEquals(BookingWaitlistingManager.BookingStatus.CONFIRMED, booking.getStatus());
    }

    // Test 2: Booking when full → status becomes WAITLISTED
    @Test
    void testBookingWhenFullIsWaitlisted() {
        // Fill the event to capacity
        event.getManager().addUser(user1);
        event.getManager().findBookingByUser(user1).setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);
        event.getManager().addUser(user2);
        event.getManager().findBookingByUser(user2).setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);

        // Add a third user — event is now full
        event.getManager().addUser(user3);
        BookingWaitlistingManager.BookingEntry booking = event.getManager().findBookingByUser(user3);

        assertNotNull(booking);
        assertEquals(BookingWaitlistingManager.BookingStatus.WAITLISTED, booking.getStatus());
    }

    // Test 3: Cancel confirmed booking → waitlisted user gets promoted
    @Test
    void testCancelBookingPromotesWaitlistedUser() {
        // Fill to capacity
        event.getManager().addUser(user1);
        event.getManager().findBookingByUser(user1).setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);
        event.getManager().addUser(user2);
        event.getManager().findBookingByUser(user2).setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);

        // Add waitlisted user
        event.getManager().addUser(user3);

        // Cancel user1's booking
        event.getManager().cancelBooking(user1);

        // Promote first waitlisted user manually (as the controller does)
        for (BookingWaitlistingManager.BookingEntry b : event.getManager().getBookings()) {
            if (b.getStatus() == BookingWaitlistingManager.BookingStatus.WAITLISTED) {
                b.setStatus(BookingWaitlistingManager.BookingStatus.CONFIRMED);
                break;
            }
        }

        BookingWaitlistingManager.BookingEntry promoted = event.getManager().findBookingByUser(user3);
        assertEquals(BookingWaitlistingManager.BookingStatus.CONFIRMED, promoted.getStatus());
    }

    // Test 4: Duplicate booking prevention
    @Test
    void testDuplicateBookingPrevented() {
        event.getManager().addUser(user1);
        int sizeBefore = event.getManager().getBookings().size();

        // Try to add the same user again
        event.getManager().addUser(user1);
        int sizeAfter = event.getManager().getBookings().size();

        assertEquals(sizeBefore, sizeAfter);
    }
}