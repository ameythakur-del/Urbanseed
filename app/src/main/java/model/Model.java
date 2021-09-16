package model;

import java.util.Date;

public class Model {
    Date date;
    Boolean aBoolean;

    public Model() {
    }

    public Model(Date date, Boolean aBoolean) {
        this.date = date;
        this.aBoolean = aBoolean;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
}
