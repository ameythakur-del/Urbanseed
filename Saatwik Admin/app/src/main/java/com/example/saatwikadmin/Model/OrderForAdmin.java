package com.example.saatwikadmin.Model;

public class OrderForAdmin {
    String item, number, price, userId, discount;

    public OrderForAdmin() {
    }

    public OrderForAdmin(String item, String number, String price, String userId, String discount) {
        this.item = item;
        this.number = number;
        this.price = price;
        this.userId = userId;
        this.discount = discount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
