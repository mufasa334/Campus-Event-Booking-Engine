package campusbooking;

import java.util.Scanner;

public class ConsoleApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CampusEventBookingSystem system = new CampusEventBookingSystem();
        while (true) {

            System.out.println("\n Campus Event Booking System ");
            System.out.println("1. View Events");
            System.out.println("2. Create Booking");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. View User Bookings");
            System.out.println("6. View All Users");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println(" Invalid input.");
                continue;
            }

            switch (choice) {


                case 1 -> {
                    System.out.println("\n EVENTS ");

                    system.listEvents().forEach(e ->
                            System.out.println(
                                    e.getEventId() + " | " +
                                            e.getTitle() + " | " +
                                            e.getLocation() + " | " +
                                            e.getConfirmed().size() + "/" +
                                            e.getCapacity()
                            )
                    );
                }


                case 2 -> {
                    System.out.print("Enter User ID (e.g., U1): ");
                    String userId = sc.nextLine();

                    System.out.print("Enter Event ID (e.g., E1): ");
                    String eventId = sc.nextLine();

                    boolean success = system.createBooking(userId, eventId);

                    if (success) {
                        System.out.println(" Booking created successfully.");
                    } else {
                        System.out.println(" Failed to create booking (invalid ID, duplicate, or full).");
                    }
                }


                case 3 -> {
                    if (!system.hasBookings()) {
                        System.out.println("⚠ No bookings exist. Nothing to cancel.");
                        break;
                    }

                    System.out.print("Enter Booking ID (e.g., B1): ");
                    String bookingId = sc.nextLine();

                    boolean success = system.cancelBooking(bookingId);

                    if (success) {
                        System.out.println(" Booking cancelled.");
                    } else {
                        System.out.println(" Booking not found.");
                    }
                }


                case 4 -> {
                    if (!system.hasBookings()) {
                        System.out.println("⚠ No bookings available.");
                        break;
                    }

                    System.out.println("\n ALL BOOKINGS ");
                    system.getAllBookings().forEach(b ->
                            System.out.println(
                                    b.getBookingId() + " | " +
                                            b.getUser().getName() + " -> " +
                                            b.getEvent().getTitle()
                            )
                    );
                }


                case 5 -> {
                    if (!system.hasBookings()) {
                        System.out.println("⚠ No bookings exist.");
                        break;
                    }

                    System.out.print("Enter User ID: ");
                    String userId = sc.nextLine();

                    var list = system.getUserBookings(userId);

                    if (list.isEmpty()) {
                        System.out.println("No bookings found for this user.");
                    } else {
                        System.out.println("\n--- BOOKINGS FOR " + userId + " ---");
                        list.forEach(b ->
                                System.out.println(
                                        b.getBookingId() + " | " +
                                                b.getEvent().getTitle()
                                )
                        );
                    }
                }


                case 6 -> {
                    System.out.println("\n USERS ");

                    system.listUsers().forEach(u ->
                            System.out.println(
                                    u.getUserId() + " | " +
                                            u.getName() + " | " +
                                            u.getRole()
                            )
                    );
                }


                case 0 -> {
                    System.out.println("Goodbye.");
                    return;
                }

                default -> System.out.println(" Invalid option.");
            }
        }
    }
}