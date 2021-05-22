package com.example.saatwikadmin.Utils;

import android.app.Application;

public class Users extends Application {
    private String Name;
    private String UserId;
    private String Mobile, Address, Alternate;
    private static Users instance;
    private String Status;

    public static Users getInstance(){
        if(instance == null)
            instance = new Users();
        return instance;
    }

    public Users(){}

    public String getAlternate() {
        return Alternate;
    }

    public void setAlternate(String alternate) {
        Alternate = alternate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}


