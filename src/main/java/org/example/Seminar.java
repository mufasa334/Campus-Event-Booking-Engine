package org.example;
public class Seminar extends Event{

    private String speaker;

    //-------------------------------------CONSTRUCTOR------------------------------------------------

    public Seminar(int id, String title, String date, int time, String location, int capacity, String speaker) {
        super(id, title, date, time, location, capacity);
        setSpeaker(speaker);
    }

    //-----------------------------------SETTERS / GETTERS--------------------------------------------

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSpeaker() {
        return speaker;
    }

    //----------------------------------------METHODS--------------------------------------------------
}
