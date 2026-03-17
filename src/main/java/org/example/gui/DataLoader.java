package org.example.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

public class DataLoader {

    public static void loadUsers(List<User> userList) {
        InputStream input = DataLoader.class.getResourceAsStream("/data/users.csv");

        if (input == null) {
            System.out.println("Error loading users: /data/users.csv not found");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String id = data[0].trim();
                String name = data[1].trim();
                String email = data[2].trim();
                String type = data[3].trim().toUpperCase();

                if (type.equals("STUDENT")) userList.add(new Student(id, name, email));
                else if (type.equals("STAFF")) userList.add(new Staff(id, name, email));
                else if (type.equals("GUEST")) userList.add(new Guest(id, name, email));
            }

        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public static void loadEvents(List<Event> eventList) {
        InputStream input = DataLoader.class.getResourceAsStream("/data/events.csv");

        if (input == null) {
            System.out.println("Error loading events: /data/events.csv not found");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String id = data[0].trim();
                String title = data[1].trim();
                LocalDateTime time = LocalDateTime.parse(data[2].trim());
                String loc = data[3].trim();
                int cap = Integer.parseInt(data[4].trim());
                String type = data[6].trim().toUpperCase();

                if (type.equals("WORKSHOP")) eventList.add(new Workshop(id, title, time, loc, cap, data[7].trim()));
                else if (type.equals("SEMINAR")) eventList.add(new Seminar(id, title, time, loc, cap, data[8].trim()));
                else if (type.equals("CONCERT")) eventList.add(new Concert(id, title, time, loc, cap, data[9].trim()));
            }

        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }

    public static void loadBookings(List<User> userList, List<Event> eventList) {
        InputStream input = DataLoader.class.getResourceAsStream("/data/bookings.csv");

        if (input == null) {
            System.out.println("Error loading bookings: /data/bookings.csv not found");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String userId = data[1].trim();
                String eventId = data[2].trim();

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
                    targetEvent.getManager().addUser(targetUser.getName());
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }
}