package com.example.saatwikadmin.Model;

public class OrderForAdmin {
    String item, number, price, userId, discount, charge, paid, quantity;

    public OrderForAdmin() {
    }

    public OrderForAdmin(String item, String number, String price, String userId, String discount, String charge, String paid, String quantity) {
        this.item = item;
        this.number = number;
        this.price = price;
        this.userId = userId;
        this.discount = discount;
        this.charge = charge;
        this.paid = paid;
        this.quantity = quantity;
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

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
