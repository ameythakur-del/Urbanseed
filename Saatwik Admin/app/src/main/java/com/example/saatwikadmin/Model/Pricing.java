package com.example.saatwikadmin.Model;

public class Pricing {
    String min, charge, discount, k;

    public Pricing(String min, String charge, String discount, String k) {
        this.min = min;
        this.charge = charge;
        this.discount = discount;
        this.k = k;
    }

    public Pricing() {
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getDiscount() {
        return discount;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
