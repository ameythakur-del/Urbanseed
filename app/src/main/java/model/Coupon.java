package model;

public class Coupon {
    String code, percent;

    public Coupon() {
    }

    public Coupon(String code, String percent) {
        this.code = code;
        this.percent = percent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
