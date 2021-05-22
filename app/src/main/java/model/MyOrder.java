package model;

public class MyOrder {
    private String item;
    private String taste;
    private String imageUrl;
    private String price;
    private String per;
    private String delivery;
    private String category;
    private String number;
    private String userPhone;
    private String status, message;
    private String discount;

    public MyOrder() {
    }

    public MyOrder(String item, String taste, String imageUrl, String price, String per, String delivery, String category, String number, String userPhone, String status, String message, String discount) {
        this.item = item;
        this.taste = taste;
        this.imageUrl = imageUrl;
        this.price = price;
        this.per = per;
        this.delivery = delivery;
        this.category = category;
        this.number = number;
        this.userPhone = userPhone;
        this.status = status;
        this.message = message;
        this.discount = discount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
