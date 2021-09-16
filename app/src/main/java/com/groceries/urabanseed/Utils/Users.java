package com.groceries.urabanseed.Utils;

import android.app.Application;

public class Users extends Application {
    private String Name;
    private String UserNumber;
    private String Address, Pincode, email;
    private static Users instance;

    public static Users getInstance(){
        if(instance == null)
            instance = new Users();
        return instance;
    }

    public Users(){}

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getUserNumber() {
        return UserNumber;
    }

    public void setUserNumber(String userNumber) {
        UserNumber = userNumber;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


