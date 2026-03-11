package org.example.gui;
import java.time.LocalDateTime;

public class Seminar extends Event{

    private String speakerName;

    //-------------------------------------CONSTRUCTOR------------------------------------------------

    public Seminar(String eventId, String title, LocalDateTime dateTime, String location, int capacity, String speakerName) {
        // Must match the 6-argument constructor in Event.java
        super(eventId, title, dateTime, location, capacity, EventType.SEMINAR);
        this.speakerName = speakerName;
    }

    //-----------------------------------SETTERS / GETTERS--------------------------------------------

    public String getSpeakerName() { return speakerName; }
    public void setSpeakerName(String speakerName) { this.speakerName = speakerName; }
    public String getSpecificAttribute() { return speakerName; }
    //----------------------------------------METHODS--------------------------------------------------
}
