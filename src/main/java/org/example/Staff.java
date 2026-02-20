package org.example;
public class Staff extends User {

    //private Event[] booking = new Event[5];

    //----------------------------------CONSTRUCTOR-----------------------------------------------

    public Staff(String id, String name, String email) {
        super(id, name, email, UserType.STAFF, 5);
    }

    //---------------------------------SETTERS / GETTERS--------------------------------------------

    //--------------------------------------METHODS-------------------------------------------------

    //A METHOD THAT PRINTS OUT ALL OF THIS PERSON'S DETAILS
    /*public void details() {
        System.out.println("USER ID:         " + super.getUserId());
        System.out.println("NAME:            " + super.getName());
        System.out.println("EMAIL:           " + super.getEmail());
        System.out.println("VISITOR TYPE:    Staff");

        System.out.print("FIRST BOOKING:   ");
        if(booking[0] != null) System.out.println(booking[0].getTitle());
        else System.out.println("Null");

        System.out.print("SECOND BOOKING:  ");
        if(booking[1] != null) System.out.println(booking[1].getTitle());
        else System.out.println("Null");

        System.out.print("THIRD BOOKING:   ");
        if(booking[2] != null) System.out.println(booking[2].getTitle());
        else System.out.println("Null");

        System.out.print("FOURTH BOOKING:  ");
        if(booking[3] != null) System.out.println(booking[3].getTitle());
        else System.out.println("Null");

        System.out.print("FIFTH BOOKING:   ");
        if(booking[4] != null) System.out.println(booking[4].getTitle());
        else System.out.println("Null");
    }

    //A METHOD THAT WILL BOOK AN EVENT USING AN EMPTY BOOKING SLOT
    //PRECONDITION: ANY [EVENT] TO BE BOOKED WITH A BOOKING ARRAY
    public void book(Event event) {

        boolean again = true;
        for(int i = 0; i < 5 && again; i++) {
            if(booking[i] == null && event.hasSpace()) {
                event.addAttendance(this);
                again = false;
                booking[i] = event;
            }
        }
    }*/


}
