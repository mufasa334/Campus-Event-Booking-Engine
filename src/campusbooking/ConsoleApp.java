package campusbooking;

import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {

        CampusEventBookingSystem system = new CampusEventBookingSystem();
        system.loadSampleData();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n Campus Event Booking    ");
            System.out.println("1) List Users");
            System.out.println("2) List Events");
            System.out.println("3) Create Booking");
            System.out.println("4) Cancel Booking");
            System.out.println("5) List Bookings");
            System.out.println("0) Exit");

            System.out.print("Choose: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1 -> {
                    for (User u : system.users().listUsers()) {
                        System.out.println(u.getUserId() + " " + u.getName());
                    }
                }

                case 2 -> {
                    for (Event e : system.events().listEvents()) {
                        System.out.println(
                                "ID: " + e.getEventId() +
                                        " | Title: " + e.getTitle() +
                                        " | Location: " + e.getLocation() +
                                        " | Capacity: " + e.getCapacity()
                        );
                    }
                }

                case 3 -> {
                    System.out.print("User ID: ");
                    String userId = sc.nextLine();

                    System.out.print("Event ID: ");
                    String eventId = sc.nextLine();

                    Booking b = system.bookings().createBooking(userId, eventId);
                    System.out.println("Booking created: " + b.getBookingId());
                }

                case 4 -> {
                    var bookings = system.bookings().listBookings();

                    if (bookings.isEmpty()) {
                        System.out.println("No bookings available.");
                    } else {
                        System.out.print("Booking ID: ");
                        String bookingId = sc.nextLine();

                        system.bookings().cancelBooking(bookingId);
                        System.out.println("Booking cancelled.");
                    }
                }

                case 5 -> {
                    var bookings = system.bookings().listBookings();

                    if (bookings.isEmpty()) {
                        System.out.println("No bookings available.");
                    } else {
                        for (Booking b : bookings) {
                            System.out.println(b.getBookingId()
                                    + " user=" + b.getUserId()
                                    + " event=" + b.getEventId()
                                    + " status=" + b.getStatus());
                        }
                    }
                }

                case 0 -> {
                    System.out.println("Goodbye.");
                    return;
                }
            }
        }
    }
}