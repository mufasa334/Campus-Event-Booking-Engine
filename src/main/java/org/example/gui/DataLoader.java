package org.example.gui;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List; // THE FIX: Changed from ArrayList to List

public class DataLoader {

    // THE FIX: Changed ArrayList<User> to List<User>
    public static void loadUsers(String filePath, List<User> userList) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header row
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                String email = data[2];
                String type = data[3].toUpperCase();

                if (type.equals("STUDENT")) userList.add(new Student(id, name, email));
                else if (type.equals("STAFF")) userList.add(new Staff(id, name, email));
                else if (type.equals("GUEST")) userList.add(new Guest(id, name, email));
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // THE FIX: Changed ArrayList<Event> to List<Event>
    public static void loadEvents(String filePath, List<Event> eventList) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // data[0]=id, [1]=title, [2]=time, [3]=loc, [4]=cap, [5]=status, [6]=type, [7]=topic, [8]=speaker, [9]=age
                String id = data[0];
                String title = data[1];
                LocalDateTime time = LocalDateTime.parse(data[2]);
                String loc = data[3];
                int cap = Integer.parseInt(data[4]);
                String type = data[6].toUpperCase();

                if (type.equals("WORKSHOP")) eventList.add(new Workshop(id, title, time, loc, cap, data[7]));
                else if (type.equals("SEMINAR")) eventList.add(new Seminar(id, title, time, loc, cap, data[8]));
                else if (type.equals("CONCERT")) eventList.add(new Concert(id, title, time, loc, cap, data[9]));
            }
        } catch (IOException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }


    public static void loadBookings(String filePath, List<User> userList, List<Event> eventList) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header row

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // data[0]=bookingId, [1]=userId, [2]=eventId, [3]=createdAt, [4]=status
                String userId = data[1];
                String eventId = data[2];

                // 1. Find the User object
                User targetUser = null;
                for (User u : userList) {
                    // NOTE: If your User class uses getId() instead of getUserId(), change it here!
                    if (u.getUserId().equals(userId)) {
                        targetUser = u;
                        break;
                    }
                }

                // 2. Find the Event object
                Event targetEvent = null;
                for (Event e : eventList) {
                    if (e.getEventId().equals(eventId)) {
                        targetEvent = e;
                        break;
                    }
                }

                // 3. If both were found, add the user to the event's manager
                if (targetUser != null && targetEvent != null) {
                    targetEvent.getManager().addUser(targetUser.getName());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }

}