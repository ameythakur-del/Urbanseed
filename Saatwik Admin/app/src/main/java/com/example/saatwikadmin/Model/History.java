package com.example.saatwikadmin.Model;

public class History {
    String userPhone, item, time, number, price;

    public History(String userPhone, String item, String time, String number, String price) {
        this.userPhone = userPhone;
        this.item = item;
        this.time = time;
        this.number = number;
        this.price = price;
    }

    public History() {
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
