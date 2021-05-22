package model;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

public class Item {
    private String item;
    private String taste;
    private String imageUrl;
    private String price;
    private String per;
    private String delivery;
    private String category;
    private String original;
    private String available;
    private Boolean visibility;

    public Item() {
    }

    public Item(String item, String taste, String imageUrl, String price,String per, String delivery, String category, String original, String available, Boolean visibility) {
        this.item = item;
        this.taste = taste;
        this.imageUrl = imageUrl;
        this.price = price;
        this.per = per;
        this.delivery = delivery;
        this.category = category;
        this.original = original;
        this.available = available;
        this.visibility = visibility;
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

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}