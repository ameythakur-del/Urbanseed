package model;

public class CartItem {
    private String item;
    private String taste;
    private String imageUrl;
    private String price;
    private String per;
    private String delivery;
    private String category;
    private String number;
    private String userPhone;
    private String time;
    private String discount;
    private String original;

    public CartItem() {
    }

    public CartItem(String item, String taste, String imageUrl, String price, String per, String delivery, String category, String number, String userPhone, String time, String discount, String original) {
        this.item = item;
        this.taste = taste;
        this.imageUrl = imageUrl;
        this.price = price;
        this.per = per;
        this.delivery = delivery;
        this.category = category;
        this.number = number;
        this.userPhone = userPhone;
        this.time = time;
        this.discount = discount;
        this.original = original;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
