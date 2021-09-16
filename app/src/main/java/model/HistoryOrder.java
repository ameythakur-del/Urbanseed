package model;

public class HistoryOrder {
    String item, number, price, quantity, charge;

    public HistoryOrder() {
    }

    public HistoryOrder(String item, String number, String price, String quantity, String charge) {
        this.item = item;
        this.number = number;
        this.price = price;
        this.quantity = quantity;
        this.charge = charge;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }
}
