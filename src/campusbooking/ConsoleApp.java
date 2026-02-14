package campusbooking;

import campusbooking.model.*;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {

        CampusEventBookingSystem system = new campusbooking.campusbooking.CampusEventBookingSystem();
        system.loadSampleData();   // simple in-memory test data

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Phase 1 Console Demo ===");
            System.out.println("1) List Users");
            System.out.println("2) List Events");
            System.out.println("3) Create Booking");
            System.out.println("0) Exit");

            int choice = readInt(sc, "Choose: ");

            switch (choice) {
                case 1 -> listUsers(system);
                case 2 -> listEvents(system);
                case 3 -> createBooking(system, sc);
                case 0 -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    //users

    private static void listUsers(CampusEventBookingSystem system) {
        List<User> users = system.users().listUsers();
        System.out.println("\n--- Users ---");
        for (User u : users) {
            System.out.println(
                    u.getUserId() + " | " +
                            u.getName() + " | " +
                            u.getUserType()
            );
        }
    }

    // events

    private static void listEvents(CampusEventBookingSystem system) {
        List<Event> events = system.events().listEvents();
        System.out.println("\n--- Events ---");
        for (Event e : events) {
            System.out.println(
                    e.getEventId() + " | " +
                            e.getTitle() + " | " +
                            e.getDateTime() + " | cap=" +
                            e.getCapacity()
            );
        }
    }

    //bookings

    private static void createBooking(CampusEventBookingSystem system, Scanner sc) {
        String userId = readLine(sc, "Enter userId: ");
        String eventId = readLine(sc, "Enter eventId: ");

        Booking b = system.bookings().createBooking(userId, eventId);

        if (b != null) {
            System.out.println("Created booking: " +
                    b.getBookingId() + " | status=" +
                    b.getStatus());
        } else {
            System.out.println("Booking failed.");
        }
    }

    //extra

    private static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter an integer.");
            }
        }
    }
}