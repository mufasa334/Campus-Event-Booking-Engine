package org.example.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataSaver {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static void saveUsers(String filePath, List<User> userList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
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

            System.out.println("SUCCESS: Users saved to " + filePath);

        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static void saveEvents(String filePath, List<Event> eventList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
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

            System.out.println("SUCCESS: Events saved to " + filePath);

        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    public static void saveBookings(String filePath, List<User> userList, List<Event> eventList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("bookingId,userId,eventId,createdAt,bookingStatus");
            bw.newLine();

            for (Event event : eventList) {
                BookingWaitlistingManager mgr = event.getManager();

                // 🔥 NEW: use BookingEntry instead of User
                for (BookingWaitlistingManager.BookingEntry booking : mgr.getBookings()) {

                    User user = booking.getUser();

                    bw.write(
                            booking.getBookingId() + "," +
                                    user.getUserId() + "," +
                                    event.getEventId() + "," +
                                    booking.getCreatedAt().format(FORMATTER) + "," +
                                    booking.getStatus()   // CONFIRMED / WAITLISTED / CANCELLED
                    );

                    bw.newLine();
                }
            }

            System.out.println("SUCCESS: Bookings saved to " + filePath);

        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }



    public static void saveAll(List<User> userList, List<Event> eventList) {
        saveUsers("src/main/resources/data/users.csv", userList);
        saveEvents("src/main/resources/data/events.csv", eventList);
        saveBookings("src/main/resources/data/bookings.csv", userList, eventList);
    }
}