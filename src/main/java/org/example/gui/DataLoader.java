package org.example.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class DataLoader {

    private static final Path DATA_DIR = Path.of("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.csv");
    private static final Path EVENTS_FILE = DATA_DIR.resolve("events.csv");
    private static final Path BOOKINGS_FILE = DATA_DIR.resolve("bookings.csv");

    public static void loadUsers(List<User> userList) {
        System.out.println("Loading users from: " + USERS_FILE.toAbsolutePath());

        if (!Files.exists(USERS_FILE)) {
            System.out.println("Error loading users: " + USERS_FILE.toAbsolutePath() + " not found");
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(USERS_FILE)) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 4) continue;

                String id = data[0].trim();
                String name = data[1].trim();
                String email = data[2].trim();
                String type = data[3].trim().toUpperCase();

                if (type.equals("STUDENT")) {
                    userList.add(new Student(id, name, email));
                } else if (type.equals("STAFF")) {
                    userList.add(new Staff(id, name, email));
                } else if (type.equals("GUEST")) {
                    userList.add(new Guest(id, name, email));
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public static void loadEvents(List<Event> eventList) {
        System.out.println("Loading events from: " + EVENTS_FILE.toAbsolutePath());

        if (!Files.exists(EVENTS_FILE)) {
            System.out.println("Error loading events: " + EVENTS_FILE.toAbsolutePath() + " not found");
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(EVENTS_FILE)) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);

                if (data.length < 10) {
                    System.out.println("Skipping bad event row: " + line);
                    continue;
                }

                String id = data[0].trim();
                String title = data[1].trim();
                LocalDateTime time = LocalDateTime.parse(data[2].trim());
                String loc = data[3].trim();
                int cap = Integer.parseInt(data[4].trim());
                String type = data[6].trim().toUpperCase();

                if (type.equals("WORKSHOP")) {
                    eventList.add(new Workshop(id, title, time, loc, cap, data[7].trim()));
                } else if (type.equals("SEMINAR")) {
                    eventList.add(new Seminar(id, title, time, loc, cap, data[8].trim()));
                } else if (type.equals("CONCERT")) {
                    eventList.add(new Concert(id, title, time, loc, cap, data[9].trim()));
                }
            }

            System.out.println("Events loaded: " + eventList.size());

        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }

    public static void loadBookings(List<User> userList, List<Event> eventList) {
        System.out.println("Loading bookings from: " + BOOKINGS_FILE.toAbsolutePath());

        if (!Files.exists(BOOKINGS_FILE)) {
            System.out.println("Error loading bookings: " + BOOKINGS_FILE.toAbsolutePath() + " not found");
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(BOOKINGS_FILE)) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);
                if (data.length < 5) continue;

                String bookingId = data[0].trim();
                String userId = data[1].trim();
                String eventId = data[2].trim();
                LocalDateTime createdAt = LocalDateTime.parse(data[3].trim());
                String statusText = data[4].trim().toUpperCase();

                User targetUser = null;
                for (User u : userList) {
                    if (u.getUserId().equals(userId)) {
                        targetUser = u;
                        break;
                    }
                }

                Event targetEvent = null;
                for (Event e : eventList) {
                    if (e.getEventId().equals(eventId)) {
                        targetEvent = e;
                        break;
                    }
                }

                if (targetUser != null && targetEvent != null) {
                    BookingWaitlistingManager.BookingStatus status =
                            BookingWaitlistingManager.BookingStatus.valueOf(statusText);

                    targetEvent.getManager().addLoadedBooking(targetUser, bookingId, createdAt, status);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }
}