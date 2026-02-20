package org.example;
import java.time.LocalDateTime;

public class Seminar extends Event{

    private String speakerName;

    //-------------------------------------CONSTRUCTOR------------------------------------------------

    public Seminar(String id, String title, LocalDateTime dateTime, String location, int capacity, String speakerName) {
        super(id, title, dateTime, location, capacity, EventType.SEMINAR);
        setSpeakerName(speakerName);
    }

    //-----------------------------------SETTERS / GETTERS--------------------------------------------

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    //----------------------------------------METHODS--------------------------------------------------
}
