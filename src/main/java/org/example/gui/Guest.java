package org.example.gui;
public class Guest extends User {
    //private Event booking;

    //----------------------------------CONSTRUCTOR------------------------------------------------

    public Guest(String id, String name, String email) {
        super(id, name, email, UserType.GUEST, 1);
    }

    //---------------------------------SETTER / GETTER---------------------------------------------------

    //maybe something that returns bookings

    //--------------------------------------METHODS------------------------------------------------

    //A METHOD THAT PRINTS OUT ALL OF THIS PERSON'S DETAILS
    /*public void details() {
        System.out.println("USER ID:         " + super.getUserId());
        System.out.println("NAME:            " + super.getName());
        System.out.println("EMAIL:           " + super.getEmail());
        System.out.println("VISITOR TYPE:    Guest");

        System.out.print("CURRENT BOOKING: ");
        if(booking != null) System.out.println(booking.getTitle());
        else System.out.println("Null");
    }

    //A METHOD THAT WILL BOOK AN EVENT USING AN EMPTY BOOKING SLOT
    //PRECONDITION: ANY [EVENT] TO BE BOOKED WITH A BOOKING VARIABLE
    public void book(Event event) {

        if(booking == null && event.hasSpace()) {
            event.addAttendance(this);
            booking = event;
        }
    }*/
}
