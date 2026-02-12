package org.example;
public abstract class Person {

    private int id;
    private String name;
    private String email;

    //-----------------------------------CONSTRUCTOR--------------------------------------------

    public Person(int id, String name, String email) {

        setId(id);
        setName(name);
        setEmail(email);
    }

    //---------------------------------SETTERS / GETTERS------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    //---------------------------------METHODS----------------------------------------------------

    //A METHOD FOR EACH SUBCLASS THAT WILL PRINT OUT THEIR DETAILS
    public abstract void details();

    //A METHOD FOR EACH SUBCLASS THAT WILL BOOK AN EVENT USING AN EMPTY BOOKING SLOT
    //PRECONDITION: ANY [EVENT] TO BE BOOKED WITH A BOOKING VARIABLE
    public abstract void book(Event event);
}

