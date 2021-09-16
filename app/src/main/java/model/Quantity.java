package model;

public class Quantity {
    String quantity, priced, mrp;

    public Quantity() {
    }

    public Quantity(String quantity, String priced, String mrp) {
        this.quantity = quantity;
        this.priced = priced;
        this.mrp = mrp;
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

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}
