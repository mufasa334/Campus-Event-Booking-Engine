package org.example.gui;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataSaver {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final Path DATA_DIR = Path.of("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.csv");
    private static final Path EVENTS_FILE = DATA_DIR.resolve("events.csv");
    private static final Path BOOKINGS_FILE = DATA_DIR.resolve("bookings.csv");

    static {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.out.println("Error creating data directory: " + e.getMessage());
        }
    }

    public static void saveUsers(List<User> userList) {
        System.out.println("Saving users to: " + USERS_FILE.toAbsolutePath());

        try (BufferedWriter bw = Files.newBufferedWriter(USERS_FILE)) {
            bw.write("userId,name,email,userType");
            bw.newLine();

            for (User user : userList) {
                String type = user.getUserType().toString();
                String formattedType = type.charAt(0) + type.substring(1).toLowerCase();

                bw.write(user.getUserId() + ","
                        + user.getName() + ","
                        + user.getEmail() + ","
                        + formattedType);
                bw.newLine();
            }

            System.out.println("SUCCESS: Users saved.");

        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static void saveEvents(List<Event> eventList) {
        System.out.println("Saving events to: " + EVENTS_FILE.toAbsolutePath());

        try (BufferedWriter bw = Files.newBufferedWriter(EVENTS_FILE)) {
            bw.write("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction");
            bw.newLine();

            for (Event event : eventList) {
                String eventId = event.getEventId();
                String title = event.getTitle();
                String dateTime = event.getDateTime() != null ? event.getDateTime().format(FORMATTER) : "";
                String location = event.getLocation();
                int capacity = event.getCapacity();

                String status = (event.getStatus() == Event.EventStatus.CANCELLED) ? "Cancelled" : "Active";

                String eventType = event.getEventType().toString();
                String formattedType = eventType.charAt(0) + eventType.substring(1).toLowerCase();

                String topic = "";
                String speakerName = "";
                String ageRestriction = "";

                if (event instanceof Workshop) {
                    topic = ((Workshop) event).getTopic();
                } else if (event instanceof Seminar) {
                    speakerName = ((Seminar) event).getSpeakerName();
                } else if (event instanceof Concert) {
                    ageRestriction = ((Concert) event).getAgeRestriction();
                }

                bw.write(eventId + ","
                        + title + ","
                        + dateTime + ","
                        + location + ","
                        + capacity + ","
                        + status + ","
                        + formattedType + ","
                        + (event instanceof Workshop ? (topic.isEmpty() ? "N/A" : topic) : "") + ","
                        + (event instanceof Seminar ? (speakerName.isEmpty() ? "N/A" : speakerName) : "") + ","
                        + (event instanceof Concert ? (ageRestriction.isEmpty() ? "N/A" : ageRestriction) : ""));
                bw.newLine();
            }

            System.out.println("SUCCESS: Events saved.");

        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    public static void saveBookings(List<Event> eventList) {
        System.out.println("Saving bookings to: " + BOOKINGS_FILE.toAbsolutePath());

        try (BufferedWriter bw = Files.newBufferedWriter(BOOKINGS_FILE)) {
            bw.write("bookingId,userId,eventId,createdAt,bookingStatus");
            bw.newLine();

            for (Event event : eventList) {
                BookingWaitlistingManager mgr = event.getManager();

                for (BookingWaitlistingManager.BookingEntry booking : mgr.getBookings()) {
                    User user = booking.getUser();

                    bw.write(
                            booking.getBookingId() + "," +
                                    user.getUserId() + "," +
                                    event.getEventId() + "," +
                                    booking.getCreatedAt().format(FORMATTER) + "," +
                                    booking.getStatus()
                    );
                    bw.newLine();
                }
            }

            System.out.println("SUCCESS: Bookings saved.");

        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    public static void saveAll(List<User> userList, List<Event> eventList) {
        saveUsers(userList);
        saveEvents(eventList);
        saveBookings(eventList);
    }
}