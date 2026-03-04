package org.example.gui;
import java.time.LocalDateTime;

public class Concert extends Event{

    private String ageRestriction;

    //------------------------------------CONSTRUCTOR------------------------------------------------

    public Concert(String eventId, String title, LocalDateTime dateTime, String location, int capacity, String ageRestriction) {
        // Must match the 6-argument constructor in Event.java
        super(eventId, title, dateTime, location, capacity, EventType.CONCERT);
        this.ageRestriction = ageRestriction;
    }

    //----------------------------------SETTERS / GETTERS-------------------------------------------

    public String getAgeRestriction() { return ageRestriction; }
    public void setAgeRestriction(String ageRestriction) { this.ageRestriction = ageRestriction; }

    //---------------------------------------METHODS---------------------------------------------------
}

