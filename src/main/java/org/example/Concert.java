package org.example;
public class Concert extends Event{

    private int ageReg;

    //------------------------------------CONSTRUCTOR------------------------------------------------

    public Concert(int id, String title, String date, int time, String location, int capacity, int ageReg) {

        super(id, title, date, time, location, capacity);
        setAgeReg(ageReg);
    }

    //----------------------------------SETTERS / GETTERS-------------------------------------------

    public void setAgeReg(int ageReg) {
        this.ageReg = ageReg;
    }

    public int getAgeReg() {
        return ageReg;
    }

    //---------------------------------------METHODS---------------------------------------------------
}

