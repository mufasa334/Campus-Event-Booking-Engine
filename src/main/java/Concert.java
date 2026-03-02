import java.time.LocalDateTime;

public class Concert extends Event{

    private String ageRestriction;

    //------------------------------------CONSTRUCTOR------------------------------------------------

    public Concert(String id, String title, LocalDateTime dateTime, String location, int capacity, String ageRestriction) {

        super(id, title, dateTime, location, capacity, EventType.CONCERT);
        setAgeRestriction(ageRestriction);
    }

    //----------------------------------SETTERS / GETTERS-------------------------------------------

    public void setAgeRestriction(String ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    //---------------------------------------METHODS---------------------------------------------------
}

