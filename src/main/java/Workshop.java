import java.time.LocalDateTime;

public class Workshop extends Event{

    private String topic;

    //----------------------------------CONSTRUCTOR--------------------------------------------------

    public Workshop(String id, String title, LocalDateTime dateTime, String location, int capacity, String topic) {
        super(id, title, dateTime, location, capacity, EventType.WORKSHOP);
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
