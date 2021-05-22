package model;

public class OrderForAdmin {
    String item, number, price, userPhone, discount;

    public OrderForAdmin() {
    }

    public OrderForAdmin(String item, String number, String price, String userPhone, String discount) {
        this.item = item;
        this.number = number;
        this.price = price;
        this.userPhone = userPhone;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
