package org.example.gui;
public class Student extends User {

    //private Event[] booking = new Event[3];

    //------------------------------------CONSTRUCTOR------------------------------------------------

    public Student(String id, String name, String email) {
        super(id, name, email, UserType.STUDENT, 3);
    }

    //----------------------------------SETTERS / GETTERS---------------------------------------------

    //maybe something that returns bookings

    //---------------------------------------METHODS--------------------------------------------------

    //A METHOD THAT PRINTS OUT ALL OF THIS PERSON'S DETAILS
    /*public void details() {
        System.out.println("USER ID:         " + super.getUserId());
        System.out.println("NAME:            " + super.getName());
        System.out.println("EMAIL:           " + super.getEmail());
        System.out.println("VISITOR TYPE:    Student");

        System.out.print("FIRST BOOKING:   ");
        if(booking[0] != null) System.out.println(booking[0].getTitle());
        else System.out.println("Null");

        System.out.print("SECOND BOOKING:  ");
        if(booking[1] != null) System.out.println(booking[1].getTitle());
        else System.out.println("Null");

        System.out.print("THIRD BOOKING:   ");
        if(booking[2] != null) System.out.println(booking[2].getTitle());
        else System.out.println("Null");
    }

    //A METHOD THAT WILL BOOK AN EVENT USING AN EMPTY BOOKING SLOT
    //PRECONDITION: ANY [EVENT] TO BE BOOKED WITH A BOOKING ARRAY
    public void book(Event event) {

        boolean again = true;
        for(int i = 0; i < 3 && again; i++) {
            if(booking[i] == null && event.hasSpace()) {
                event.addAttendance(this);
                again = false;
                booking[i] = event;
            }
        }
    }*/
}
