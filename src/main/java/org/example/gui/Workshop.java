package org.example.gui;
import java.time.LocalDateTime;

public class Workshop extends Event{

    private String topic;

    //----------------------------------CONSTRUCTOR--------------------------------------------------

    public Workshop(String eventId, String title, LocalDateTime dateTime, String location, int capacity, String topic) {

        // 2. The super call MUST have 6 arguments in this exact order
        super(eventId, title, dateTime, location, capacity, EventType.WORKSHOP);

        this.topic = topic;
    }

    //--------------------------------SETTERS / GETTERS--------------------------------------------

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }
    //---------------------------------------METHODS-------------------------------------------------
}
