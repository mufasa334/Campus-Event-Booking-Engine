package org.example;
public class Workshop extends Event{

    private String topic;

    //----------------------------------CONSTRUCTOR--------------------------------------------------

    public Workshop(int id, String title, String date, int time, String location, int capacity, String topic) {
        super(id, title, date, time, location, capacity);
        setTopic(topic);
    }

    //--------------------------------SETTERS / GETTERS--------------------------------------------

    public void setTopic(String topic) {
        this.topic = topic;
    }


    public String getTopic() {
        return topic;
    }

    //---------------------------------------METHODS-------------------------------------------------
}
