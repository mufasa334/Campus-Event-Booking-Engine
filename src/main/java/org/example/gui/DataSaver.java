package org.example.gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataSaver {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    // Saves all the users to users.csv with the format of userID, name, email, and usertype

    public static void saveUsers(String filePath, List<User> userList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            bw.write("userId,name,email,userType");
            bw.newLine();

            for (User user : userList) {
                String type = user.getUserType().toString(); // STUDENT, STAFF, or GUEST
                // Capitalize properly: "Student", "Staff", "Guest"
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

    /*Saves all the events to events.csv with the format of
    eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction*/

    public static void saveEvents(String filePath, List<Event> eventList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            bw.write("eventId,title,dateTime,location,capacity,status,eventType,topic,speakerName,ageRestriction");
            bw.newLine();

            for (Event event : eventList) {
                String eventId    = event.getEventId();
                String title      = event.getTitle();
                String dateTime   = event.getDateTime() != null ? event.getDateTime().format(FORMATTER) : "";
                String location   = event.getLocation();
                int capacity      = event.getCapacity();

                // Status: the Event uses EventStatus enum (CONFIRMED/WAITLISTED/CANCELLED)
                // but the CSV expects "Active" or "Cancelled"
                String status = (event.getStatus() == Event.EventStatus.CANCELLED) ? "Cancelled" : "Active";

                String eventType  = event.getEventType().toString(); // WORKSHOP, SEMINAR, CONCERT
                // Capitalize: "Workshop", "Seminar", "Concert"
                String formattedType = eventType.charAt(0) + eventType.substring(1).toLowerCase();

                // Type-specific fields — only one will be filled, others blank
                String topic          = "";
                String speakerName    = "";
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

    /*Saves all bookings to bookings.csv with the format of
    bookingId,userId,eventId,createdAt,bookingStatus
    We reconstruct bookings by looping through every event's manager UserList.
     * Users at index < capacity are "Confirmed", the rest are "Waitlisted" */

     public static void saveBookings(String filePath, List<User> userList, List<Event> eventList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            bw.write("bookingId,userId,eventId,createdAt,bookingStatus");
            bw.newLine();

            for (Event event : eventList) {
                BookingWaitlistingManager mgr = event.getManager();
                String eventId    = event.getEventId();
                String bookingId  = mgr.getBookingID();
                String createdAt  = mgr.getCreatedAt() != null ? mgr.getCreatedAt().format(FORMATTER) : "";

                List<String> userNames = mgr.UserList;

                for (int i = 0; i < userNames.size(); i++) {
                    String name = userNames.get(i);
                    if (name == null || name.trim().isEmpty()) continue;

                    // Find the userId that matches this name
                    String userId = findUserIdByName(name, userList);
                    if (userId == null) continue; // skip if we can't match

                    // Determine status based on position vs capacity
                    String bookingStatus;
                    if (event.getStatus() == Event.EventStatus.CANCELLED) {
                        bookingStatus = "Cancelled";
                    } else if (i < event.getCapacity()) {
                        bookingStatus = "Confirmed";
                    } else {
                        bookingStatus = "Waitlisted";
                    }

                    bw.write(bookingId + ","
                            + userId + ","
                            + eventId + ","
                            + createdAt + ","
                            + bookingStatus);
                    bw.newLine();
                }
            }

            System.out.println("SUCCESS: Bookings saved to " + filePath);

        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

     //Helper: looks up a userId by matching the user's name in the user list.
     //Returns null if not found.

    private static String findUserIdByName(String name, List<User> userList) {
        for (User user : userList) {
            if (user.getName().equals(name)) {
                return user.getUserId();
            }
        }
        return null;
    }

     // Convenience method: saves ALL data in one call.
     //Call this from your Save button handler.

    public static void saveAll(List<User> userList, List<Event> eventList) {
        saveUsers("src/main/resources/data/users.csv", userList);
        saveEvents("src/main/resources/data/events.csv", eventList);
        saveBookings("src/main/resources/data/bookings.csv", userList, eventList);
    }
}
