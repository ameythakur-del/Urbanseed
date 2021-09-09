package com.example.saatwikadmin;

public class Slot {
    String starttime, charges, closetime;

    public Slot() {
    }

    public Slot(String starttime, String closetime, String charges) {
        this.starttime = starttime;
        this.closetime = closetime;
        this.charges = charges;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }
}
