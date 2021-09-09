package com.example.saatwikadmin.Model;

public class MyOrder {
    private String Name;
    private String Mobile, Address, Pincode, Time, Date;
    private String item;
    private String price;
    private String number;
    private String userId;
    private String status, message;
    private int total, x, z, f;

    public MyOrder() {
    }

    public MyOrder(String name, String mobile, String address, String pincode, String item,  String price, String number, String userId, String status, String message, int total, int x, int z, int f, String Time, String Date) {
        Time = Time;
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
        this.total = total;
        this.x = x;
        this.z = z;
        this.f = f;
        this.Date = Date;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
