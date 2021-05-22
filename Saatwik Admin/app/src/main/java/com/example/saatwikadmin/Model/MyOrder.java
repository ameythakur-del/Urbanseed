package com.example.saatwikadmin.Model;

public class MyOrder {
    private String Name;
    private String Mobile, Address, Pincode;
    private String item;
    private String price;
    private String number;
    private String userId;
    private String status, message;

    public MyOrder() {
    }

    public MyOrder(String name, String mobile, String address, String pincode, String item,  String price, String number, String userId, String status, String message) {
        Name = name;
        Mobile = mobile;
        Address = address;
        Pincode = pincode;
        this.item = item;
        this.price = price;
        this.number = number;
        this.userId = userId;
        this.status = status;
        this.message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
