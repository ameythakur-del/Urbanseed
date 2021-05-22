package model;

public class Quantity {
    String quantity, priced;

    public Quantity() {
    }

    public Quantity(String quantity, String priced) {
        this.quantity = quantity;
        this.priced = priced;
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
}
