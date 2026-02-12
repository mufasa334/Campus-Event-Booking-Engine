package org.example;
public abstract class Event {

    private int id;
    private int time;
    private int capacity;
    private boolean status;
    private String location;
    private String date;
    private String title;
    private Person[] attendees;
    private int count = 0;
    //Maybe make an arraylist for the waitlist or something
    //-----------------------------------CONSTRUCTOR--------------------------------------------------

    public Event(int id, String title, String date, int time, String location, int capacity) {

        setId(id);
        setTitle(title);
        setDate(date);
        setTime(time);
        setLocation(location);
        setCapacity(capacity);
        status = true;
        attendees = new Person[capacity];
    }

    //----------------------------SETTERS / GETTERS-------------------------------------------------

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    //--------------------------------------METHODS-------------------------------------------------

    //A METHOD FOR EACH SUBCLASS THAT ADDS PEOPLE TO THE ATTENDANCE LIST AS LONG AS THE EVENT IS NOT FULL ON CAPACITY
    //PRECONDITION: ANY [PERSON] AND ADDS THEM TO THE ATTENDEES ARRAY
    public void addAttendance(Person guy) {
        if(hasSpace()) {
            attendees[count] = guy;
            count++;
        }
        //You can put something about the waitlist here
    }

    //A METHOD THAT WILL RETURN TRUE IF THERE IS EMPTY SPACE IN THE ATTENDANCE ARRAY
    //POSTCONDITION: RETURNS [BOOLEAN] ABOUT WHETHER THERE IS EMPTY SPACE OR NOT
    public boolean hasSpace() {
        if (count < capacity) return true;
        return false;
    }

}

