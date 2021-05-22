package com.example.saatwikadmin.Model;

public class Item {
    private String item;
    private String taste;
    private String imageUrl;
    private String price;
    private String quantity;
    private String priced;
    private String delivery;
    private String category;
    private String original;
    private String subcategory;
    private Boolean visibility;

    public Item() {
    }

    public Item(String item, String taste, String imageUrl, String price, String quantity, String priced, String delivery, String category, String original, String subcategory, Boolean visibility) {
        this.item = item;
        this.taste = taste;
        this.imageUrl = imageUrl;
        this.priced = priced;
        this.quantity = quantity;
        this.price = price;
        this.delivery = delivery;
        this.category = category;
        this.original = original;
        this.subcategory = subcategory;
        this.visibility = visibility;
    }

    public Item(String toString, String toString1, String toString2, String toString3, String toString4, String toString5, String toString6, String toString7) {
    }

    public Item(String toString, String toString1, String toString2, String toString3, String toString4, String toString5, String toString6) {
    }

    public Item(String toString, String toString1) {
    }

    public Item(String toString, String toString1, String toString2, String toString3, String toString4, String toString5, String toString6, String toString7, Boolean visibility) {
    }

    public Item(String toString, String toString1, String toString2, String toString3, String toString4, String toString5, String toString6, Boolean visibility) {
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPriced() {
        return priced;
    }

    public void setPriced(String priced) {
        this.priced = priced;
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

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}