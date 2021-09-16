package model;

public class Slot {
    String starttime, charges, closetime, amount;
    Boolean bool;

    public Slot() {
    }

    public Slot(String starttime, String closetime, String charges, String amount, Boolean bool) {
        this.starttime = starttime;
        this.closetime = closetime;
        this.charges = charges;
        this.amount = amount;
        this.bool = bool;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Boolean getBool() {
        return bool;
    }

    public void setBool(Boolean bool) {
        this.bool = bool;
    }
}
